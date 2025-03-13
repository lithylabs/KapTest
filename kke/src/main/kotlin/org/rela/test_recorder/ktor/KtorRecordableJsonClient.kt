package org.rela.test_recorder.ktor

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.*
import org.rela.test_recorder.Recorder
import org.rela.test_recorder.core.JsonMapper
import org.rela.test_recorder.core.RecordingMissingCallException
import org.rela.test_recorder.core.sha256

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
object KtorRecordableJsonClient {
    @OptIn(InternalAPI::class)
    operator fun invoke(
        client: HttpClient,
        recorder: Recorder,
        keyIncludedHeaders: Boolean = false
    ) = HttpClient(
        MockEngine { request ->

            val resp = if (recorder.record) {
                handleRequestRecord(client, recorder, request)
            } else {
                handlePlayback(recorder, request)
            }

            resp
        }

    )

    /**
     * Executes the request over the network and records the details as a KtorClientEvent.
     *
     * @see KtorJsonClientEvent
     */
    @OptIn(InternalAPI::class)
    suspend fun handleRequestRecord(client: HttpClient, recorder: Recorder, request: HttpRequestData): HttpResponseData {
        val resp = client.engine.execute(request)

        val event = KtorJsonClientEvent(
            request.method.value,
            request.url.toString(),
            request.body.toString(),
            resp.statusCode.value,
            resp.headers.toMap(),
            resp.body.toString()
        )

        val eventJson = JsonMapper.jsonPretty.encodeToString(KtorJsonClientEvent.serializer(), event)
        val eventId = getKtorClientEventId(request)

        recorder.recordEvent(eventId, eventJson)

        return resp
    }

    /**
     * Fetches the event from the recorder and returns it as a response. No request is made over the network.
     */
    fun MockRequestHandleScope.handlePlayback(recorder: Recorder, request: HttpRequestData): HttpResponseData {
        val eventId = getKtorClientEventId(request)
        val eventJson = recorder.fetchPlaybackJson(eventId)
            ?: throw RecordingMissingCallException("Playback null: ${request.method} ${request.url}")
        val event = JsonMapper.json.decodeFromString<KtorJsonClientEvent>(eventJson)

        return respond(
            content = ByteReadChannel(event.respJson),
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
