package org.lithylabs.kaptest.ktor

import io.kotest.common.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.junit.jupiter.api.*
import org.lithylabs.kaptest.*


class KtorRecordableJsonClientTest {

    @Test
    fun `recordable client test`() = runBlocking {
        val recorder = MockTestRecorder()
        val recordableClient = RecordableHttpClient(CIO, recorder)

        recorder.readyTest(true)
        val resp = recordableClient.get("https://jsonplaceholder.typicode.com/todos/1")
        val json = resp.bodyAsText()
        println(json)
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