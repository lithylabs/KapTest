package org.rela.test_recorder.core

import org.rela.test_recorder.*
import java.io.*
import java.time.*

class TestRecorderImpl(
    val rootDir: String,
    val testClass: String,
    val testMethod: String,
    override val record: Boolean,
    val adjustableDateTime: AdjustableDateTime
): TestRecorder {
    val eventCounts = mutableMapOf<String, Int>()
    val recordingPath = "$rootDir/$testClass/$testMethod.json"
    val recording: Recording

    init {
        recording = if (this.record) {
            writeRecording(
                recordingPath,
                Recording(
                    ZonedDateTime.now().toString(),
                    testClass,
                    testMethod,
                    adjustableDateTime.currentTestMillis(),
                    eventMap = mutableMapOf()
                )
            )

        } else {
            readRecording(recordingPath)
        }
    }

    override fun recordEvent(eventId: String, json: String?) {
        val adjustedId = getAdjustedEventId(eventId)
        recording.eventMap[adjustedId] = RecordableEvent(adjustedId, json)
        writeRecording(recordingPath, recording)
    }

    override fun fetchPlaybackJson(callName: String): String? {
        val adjustedKey = getAdjustedEventId(callName)
        val record = recording.eventMap[adjustedKey]
            ?: throw RecordingMissingCallException("Missing Repository Call: $adjustedKey")
        return record.json
    }

    /**
     * Adjusts the eventId to be unique by adding a count to the end of the eventId.  The count
     * is based on the eventId so ordering is only important on similar eventIds.
     */
    fun getAdjustedEventId(eventId: String): String {
        val count = eventCounts.getOrDefault(eventId, 0)
        eventCounts[eventId] = count + 1
        return "${eventId}_$count"
    }


    fun writeRecording(path: String, recording: Recording): Recording {
        val recordingFile = File(path)
        recordingFile.parentFile.mkdirs()

        val raw = JsonMapper.jsonPretty.encodeToString(recording)
        recordingFile.writeText(raw)

        return recording
    }

    fun readRecording(path: String): Recording {
        val recordingFile = File(path)
        if (!recordingFile.exists()) {
            throw RecordingNotFoundException("Recording not found for test: ${recordingFile.absolutePath}")
        }

        val raw = recordingFile.readText()
        val recording = JsonMapper.json.decodeFromString<Recording>(raw)

        adjustableDateTime.setCurrentTestMillis(recording.startCurrentMillis)

        return recording
    }
}

