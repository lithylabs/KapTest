package org.lithylabs.kaptest.core

import org.lithylabs.kaptest.*
import java.io.*
import java.time.*

class TestRecorder (
    config: TestRecorderConfig,
    override val record: Boolean
): Recorder {
    val eventCounts = mutableMapOf<String, Int>()
    val recordingPath: String
    val recording: Recording
    val adjustableDateTime = config.adjustableDateTime

    constructor(record: Boolean = false): this(TestRecorderConfig(), record)

    init {
        if (record) {
            val disableBuildRecording = System.getenv("DISABLE_BUILD_RECORDING").toBoolean()
            if (disableBuildRecording) {
                throw NewRecordingGradleException("AllowTestRecording environment variable doesn't allow recording. " +
                        "Check to see if a record was left true.")
            }
        }

        val (className, methodName) = findClassMethodNames(config.ignoreTestClasses)
        recordingPath = if (!config.overridePath.isNullOrBlank()) {
            config.overridePath!!
        } else {
            val classPath = className.replace(".", "/")
            "${config.rootDir}/$classPath/$methodName.json"
        }


        recording = if (this.record) {
            writeRecording(
                recordingPath,
                Recording(
                    ZonedDateTime.now().toString(),
                    className,
                    methodName,
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

    fun findClassMethodNames(ignoreClasses: List<String>): Pair<String, String> {
        val stackTrace = Exception().stackTrace
        val stackIt = stackTrace.iterator()
        var stackElement = stackIt.next()
        while (
            stackElement.className == "org.lithylabs.kaptest.core.TestRecorder" ||
            ignoreClasses.contains(stackElement.className)
        ) {
            stackElement = stackIt.next()
        }
        return Pair(stackElement.className, stackElement.methodName)

    }
}

