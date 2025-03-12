package org.rela.test_recorder.core

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*
import org.rela.test_recorder.*
import java.io.*

open class TestRecorderConfig(
    /**
     * The `this` object of the test class used for naming the recording file.
     */
    val testObj: Any,
    /**
     * A utility for handling date and time during recording and playback.  Default ignores test date and time for tests
     * and tests will be played back as if they were recorded at the current time.  It is recommended to use a utility
     * to get all dates and times, so that tests can be written to handle specific dates and times.
     *
     * @see DateTimeCreator
     * @see TestDateTimeCreator
     */
    val adjustableDateTime: AdjustableDateTime,
    /**
     * The base path where recording should be stored. Defaults to "src/test/resources/recordings".
     *
     * Recordings will be stored in a directory structure that matches the package structure of the test class.
     */
    val rootDir: String = "src/test/resources/recordings",
    /**
     * If true, the recorder will record recordable events. If false, the recorder will play back recordable events.
     */
    val record: Boolean = false,
) {
    constructor(testObj: Any, record: Boolean = false): this(testObj,
        object : AdjustableDateTime {
            override fun currentTestMillis() = System.currentTimeMillis()
            override fun setCurrentTestMillis(millis: Long) {}
        },
        record = record
    )


    init {
        if (record) {
            val allowRecording = System.getenv("AllowTestRecording") ?: "true"
            if (allowRecording == "false") {
                throw NewRecordingGradleException("Recording isn't allowed in Gradle builds.")
            }
        }
    }

    /**
     * Creates a new recorder for the current test and either starts recording recordable events or plays them back when
     * the recordable events are called.
     *
     * @see recording
     */
    open fun startTest(): TestRecorder {
        // Create a recorder for this test
        val testClass = testObj::class
        val testName = findTestName()

        return TestRecorderImpl(
            rootDir,
            testClass.qualifiedName ?: throw RecordingUnsupportedException("Unsupported class: $testClass"),
            testName,
            record,
            adjustableDateTime
        )
    }

    fun findTestName(): String {
        val stackTrace = Exception().stackTrace
        val stackIt = stackTrace.iterator()
        var stackElement = stackIt.next()
        while (stackElement.methodName != "startRecorder") {
            stackElement = stackIt.next()
        }
        stackElement = stackIt.next()
        return stackElement.methodName

        // This implementation was used in junit 4
        // val testElement = stackIt.next().className
        // val index1 = testElement.indexOf('$') + 1
        // val possible = testElement.lastIndexOf('$')
        // val index2 = if (possible != -1) possible else testElement.length
        // val testName = testElement.substring(index1, index2).replace(' ', '_')
        // return testName
    }

    /**
     * This was the orginal implementation when using junit 4.  Leaving as a reference in case
     * junit 4 needs to be supported in the future.
     */
    fun findTestNameJunit4(): String {
        val stackTrace = Exception().stackTrace
        val stackIt = stackTrace.iterator()
        var stackElement = stackIt.next()
        while (stackElement.methodName != "startRecorder") {
            stackElement = stackIt.next()
        }
        stackElement = stackIt.next()
        val testElement = stackElement.className
        val index1 = testElement.indexOf('$') + 1
        val possible = testElement.lastIndexOf('$')
        val index2 = if (possible != -1) possible else testElement.length
        val testName = testElement.substring(index1, index2).replace(' ', '_')
        return testName
    }
}
