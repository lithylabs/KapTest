package org.rela.test_recorder.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.longs.shouldBeWithinPercentageOf
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import org.rela.test_recorder.DateTimeCreator
import java.time.Instant

class TestRecorderConfigTest {
    @Test
    fun `defaults record`() {
        val recorderConfig = TestRecorderConfig(this, true)
        val recorder = recorderConfig.startRecorder() as TestRecorderImpl

        recorder.rootDir shouldBe "src/test/resources/recordings"
        recorder.testClass shouldBe "org.rela.test_recorder.core.TestRecorderConfigTest"
        recorder.testMethod shouldBe "defaults record"
        recorder.record shouldBe true
    }

    @Test
    fun defaultsPlayback() {
        // make sure the recording file exists
        val preConfig = TestRecorderConfig(this, true)
        val preRecorder = preConfig.startRecorder()

        // test the playback
        val recorderConfig = TestRecorderConfig(this)
        val recorder = recorderConfig.startRecorder() as TestRecorderImpl

        recorder.rootDir shouldBe "src/test/resources/recordings"
        recorder.testClass shouldBe "org.rela.test_recorder.core.TestRecorderConfigTest"
        recorder.testMethod shouldBe "defaultsPlayback"
        recorder.record shouldBe false
    }
}