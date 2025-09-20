package org.lithylabs.kaptest.core

import org.lithylabs.kaptest.*

/**
 * A utility for handling date and time during recording and playback.  Default ignores test date and time for tests
 * and tests will be played back as if they were recorded at the current time.  It is recommended to use a utility
 * to get all dates and times, so that tests can be written to handle specific dates and times.
 *
 * @see DateTimeCreator
 * @see TestDateTimeCreator
 */
open class TestRecorderConfig(
    val record: Boolean = false
) {
    var rootDir: String = "src/test/resources/recordings"
    var adjustableDateTime = defaultAdjustableDateTime()
    var overridePath: String? = null
    var ignoreTestClasses = mutableListOf<String>()

    fun startTest(): TestRecorder {
        ignoreTestClasses.add("org.lithylabs.kaptest.core.TestRecorderConfig")
        return TestRecorder(this, record)
    }

    companion object {
        fun defaultAdjustableDateTime() = object : AdjustableDateTime {
            override fun currentTestMillis() = System.currentTimeMillis()
            override fun setCurrentTestMillis(millis: Long) {}
        }
    }
}
