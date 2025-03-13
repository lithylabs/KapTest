package org.rela.test_recorder

class MockTestRecorder: Recorder {
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

    fun readyTest(record: Boolean, json: String? = null) {
        this.record = record
        this.json = json
        recordCalled = false
        playbackCalled = false
    }
}