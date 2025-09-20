package org.lithylabs.kaptest.ktor

import io.kotest.common.runBlocking
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Test
import org.lithylabs.kaptest.Recorder


class KtorRecordableJsonClientTest {

    @Test
    fun `recordable client test`() = runBlocking {
        val client = HttpClient(CIO)
        val recorder = MockTestRecorder()
        val recordableClient = KtorRecordableJsonClient(client, recorder)

        recorder.readyTest(true)
        val resp = recordableClient.get("https://jsonplaceholder.typicode.com/todos/1")
        println(resp)
    }
}

class MockTestRecorder: Recorder {
    override var isRecording: Boolean = true
    var json: String? = null
    var recordCalled = false
    var playbackCalled = false

    override fun recordEvent(callName: String, json: String?) {
        this.json = json
        recordCalled = true
    }

    override fun fetchPlaybackJson(callName: String): String? {
        playbackCalled = true
        return json
    }

    fun readyTest(record: Boolean) {
        this.isRecording = record
        recordCalled = false
        playbackCalled = false
    }
}