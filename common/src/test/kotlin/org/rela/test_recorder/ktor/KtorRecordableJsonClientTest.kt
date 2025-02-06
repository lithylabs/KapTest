package org.rela.test_recorder.ktor

import io.kotest.common.runBlocking
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Test
import org.rela.test_recorder.TestRecorder


class KtorRecordableJsonClientTest {

    @Test
    fun `recordable client test`() = runBlocking {
        val client = HttpClient(CIO)
        val recorder = MockTestRecorder()
        val recordableClient = KtorRecordableJsonClient(client, recorder)

        recorder.readyTest(true)
        val resp = recordableClient.get("http://localhost:8080/service/page-service/health")
        println(resp)
    }
}

class MockTestRecorder: TestRecorder {
    override var record: Boolean = true
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
        this.record = record
        recordCalled = false
        playbackCalled = false
    }
}