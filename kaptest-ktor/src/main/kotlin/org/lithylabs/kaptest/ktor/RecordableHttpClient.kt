package org.lithylabs.kaptest.ktor

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.core.*

/**
 * Creates a Ktor client that can handle recording and playback of network requests that only handle
 * JSON data formats. This client can't handle other types of data formats.  Please let us know if
 * other formats are needed.
 *
 * This client is intended to be used as an override of the Ktor client in dependency
 * injection. It may also be used standalone in tests that do not use dependency injection.
 *
 * For an example of how to use in dependency injection, see the todo.
 */
object RecordableHttpClient {

    operator fun invoke(
        engine: HttpClientEngineFactory<*>,
        recorder: Recorder?,
        keyIncludedHeaders: Boolean = false,
        config: HttpClientConfig<*>.() -> Unit = {}
    ) = invoke(engine.create(), recorder, keyIncludedHeaders, config)

    operator fun invoke(
        engine: HttpClientEngine,
        recorder: Recorder?,
        keyIncludedHeaders: Boolean = false,
        config: HttpClientConfig<*>.() -> Unit = {}
    ): HttpClient {

        return if (recorder == null) {
            // Create a standard Ktor client.
            HttpClient(engine, config)

        } else {
            // Create a mock engine for recording and playback.
            HttpClient(
                MockEngine { request ->
                    val client = HttpClient(engine, config)
                    val resp = if (recorder.isRecording) {
                        handleRequestRecord(client, recorder, request)
                    } else {
                        handlePlayback(recorder, request)
                    }

                    resp
                },
                config
            )
        }
    }

    /**
     * Executes the request over the network and records the details as a KtorClientEvent.
     *
     * @see KtorJsonClientEvent
     */
    @OptIn(InternalAPI::class)
    suspend fun MockRequestHandleScope.handleRequestRecord(client: HttpClient, recorder: Recorder, request: HttpRequestData): HttpResponseData {
        val resp = client.engine.execute(request)
        val body = resp.body

        var bodyText: String
        when (body) {
            is ByteChannel -> {
                bodyText = body.readRemaining().readText()
            }
            else -> {
                throw Exception("Unknown response type: ${resp::class}")
            }
        }
        val json = if (bodyText.isNotBlank()) {
            JsonMapper.parse(JsonElement.serializer(), bodyText)
        } else {
            null
        }

        val event = KtorJsonClientEvent(
            request.method.value,
            request.url.toString(),
            request.body.toString(),
            resp.statusCode.value,
            resp.headers.toMap(),
            json
        )

        val eventJson = JsonMapper.jsonPretty.encodeToString(KtorJsonClientEvent.serializer(), event)
        val eventId = getKtorClientEventId(request)

        recorder.recordEvent(eventId, eventJson)

        return respond(
            content = ByteReadChannel(bodyText),
            status = HttpStatusCode.fromValue(event.respStatus),
            headers = headersOf(*event.respHeaders.map { it.key to it.value }.toTypedArray())
        )
    }


    /**
     * Fetches the event from the recorder and returns it as a response. No request is made over the network.
     */
    fun MockRequestHandleScope.handlePlayback(recorder: Recorder, request: HttpRequestData): HttpResponseData {
        val eventId = getKtorClientEventId(request)
        val eventJson = recorder.fetchPlaybackJson(eventId)
            ?: throw RecordingMissingCallException("Playback null: ${request.method} ${request.url}")
        val event = JsonMapper.json.decodeFromString<KtorJsonClientEvent>(eventJson)
        val body = if (event.respJson != null) {
            JsonMapper.json.encodeToString(JsonElement.serializer(), event.respJson)
        } else {
            ""
        }

        return respond(
            content = ByteReadChannel(body),
            status = HttpStatusCode.fromValue(event.respStatus),
            headers = headersOf(*event.respHeaders.map { it.key to it.value }.toTypedArray())
        )
    }

    /**
     * Converts Method + URL + Body to a unique identifier for less storage and faster access.
     */
    fun getKtorClientEventId(request: HttpRequestData): String {
        return (request.method.toString() + request.url.toString() + request.body.toString()).sha256()
    }
}
