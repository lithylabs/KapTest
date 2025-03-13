package org.rela.test_recorder.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.longs.shouldBeWithinPercentageOf
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import org.rela.test_recorder.DateTimeCreator
import java.time.Instant

class TestRecorderImplTest {

    @Test
    fun `simple record playback`() {
        val json = JsonMapper.json.encodeToString(TestData("Simple"))

        val testRecorderRecord = TestRecorder(true)
        testRecorderRecord.recordEvent("callName", json)

        val testRecorderPlayback = TestRecorder(false)
        val playback = testRecorderPlayback.fetchPlaybackJson("callName")
        playback.shouldNotBeNull()
        val playbackData = JsonMapper.json.decodeFromString<TestData>(playback)
        playbackData.test shouldBe "Simple"
    }

    @Test
    fun `multi call record playback`() {
        val fileName = "MultiCall"

        val json = JsonMapper.json.encodeToString(TestData("Simple"))
        val json2 = JsonMapper.json.encodeToString(TestData("Multi"))

        val testRecorderRecord = TestRecorder(true)
        testRecorderRecord.recordEvent("callName", json)
        testRecorderRecord.recordEvent("callName", json2)

        val testRecorderPlayback = TestRecorder(false)
        val playback = testRecorderPlayback.fetchPlaybackJson("callName")
        playback.shouldNotBeNull()
        val playbackData = JsonMapper.json.decodeFromString<TestData>(playback)
        playbackData.test shouldBe "Simple"

        val playback2 = testRecorderPlayback.fetchPlaybackJson("callName")
        playback2.shouldNotBeNull()
        val playbackData2 = JsonMapper.json.decodeFromString<TestData>(playback2)
        playbackData2.test shouldBe "Multi"
    }

    @Test
    fun `missing playback`() {
        val fileName = "MissingPlayback"

        shouldThrow<RecordingNotFoundException> {
            TestRecorder(false)
        }
    }

    @Test
    fun `missing call playback`() {
        val fileName = "MissingCall"

        // Create empty recording
        TestRecorder(true)

        // Playback missing call
        val testRecorderPlayback = TestRecorder(false)
        shouldThrow<RecordingMissingCallException> {
            testRecorderPlayback.fetchPlaybackJson("callName")
        }
    }

    @Test
    fun `time record and playback`() {
        val config = TestRecorderConfig().apply {
            adjustableDateTime = TestDateTimeCreator
        }

        val json = JsonMapper.json.encodeToString(TestData("Time"))

        val testTime = Instant.parse("1900-01-01T00:00:00.00Z")
        TestDateTimeCreator.setCurrentTestMillis(testTime.toEpochMilli())

        // Test time set correctly
        TestDateTimeCreator.currentTestMillis().shouldBeWithinPercentageOf(testTime.toEpochMilli(), 0.01)
        DateTimeCreator.currentMillis().shouldBeWithinPercentageOf(testTime.toEpochMilli(), 0.01)

        // Test Time should be set before TestRecorder is instantiated
        val testRecorderRecord = TestRecorder(config,true)
        testRecorderRecord.recordEvent("callName", json)

        // Reset to actual time
        TestDateTimeCreator.resetToActualTime()
        DateTimeCreator.currentMillis().shouldBeWithinPercentageOf(System.currentTimeMillis(), 0.01)

        // Test that playback of the Recorder adjusts the time.
        val testRecorderPlayback = TestRecorder(config, false)
        TestDateTimeCreator.currentTestMillis().shouldBeWithinPercentageOf(testTime.toEpochMilli(), 0.01)
        DateTimeCreator.currentMillis().shouldBeWithinPercentageOf(testTime.toEpochMilli(), 0.01)
    }


    @Serializable
    class TestData(val test: String)

}