package org.rela.test_recorder.core

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TestRecorderConfigTest {
    @Test
    fun `defaults record`() {
        val recorderConfig = TestRecorderConfig(this, true)
        val recorder = recorderConfig.startTest() as TestRecorderImpl

        recorder.rootDir shouldBe "src/test/resources/recordings"
        recorder.testClass shouldBe "org.rela.test_recorder.core.TestRecorderConfigTest"
        recorder.testMethod shouldBe "defaults record"
        recorder.record shouldBe true
    }

    @Test
    fun defaultsPlayback() {
        // make sure the recording file exists
        val preConfig = TestRecorderConfig(this, true)
        val preRecorder = preConfig.startTest()

        // test the playback
        val recorderConfig = TestRecorderConfig(this)
        val recorder = recorderConfig.startTest() as TestRecorderImpl

        recorder.rootDir shouldBe "src/test/resources/recordings"
        recorder.testClass shouldBe "org.rela.test_recorder.core.TestRecorderConfigTest"
        recorder.testMethod shouldBe "defaultsPlayback"
        recorder.record shouldBe false
    }
}