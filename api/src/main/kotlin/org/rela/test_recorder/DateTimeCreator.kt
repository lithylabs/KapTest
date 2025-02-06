package org.rela.test_recorder

import java.time.*

/**
 * Utility to get the current time in a way that can be overridden for testing.
 *
 * The internal class `TestDateTimeAdjuster` creates a pattern which makes it cumbersome and ugly
 * to adjust the time in production code, but facilitates creating a TestDateTimeCreator for testing
 * that provide an easy way to adjust the time for testing.  The TestDateTimeCreator would only exist
 * in test code and could never be used in production code.
 */
object DateTimeCreator {
    private var testDateTimeAdjuster: TestDateTimeAdjuster? = null

    fun currentMillis(): Long {
        return testDateTimeAdjuster?.currentTestMillis() ?: System.currentTimeMillis()
    }

    fun nowInstant(): Instant {
        return Instant.ofEpochMilli(currentMillis())
    }

    fun nowLocalDateTime(zoneId: ZoneId =  ZoneId.systemDefault()): LocalDateTime {
        return LocalDateTime.ofInstant(nowInstant(), zoneId)
    }

    /**
     * Sets the TestDateTimeAdjuster to override the current time for testing purposes.  Passing null will
     * reset the current time to the system time the updates normally.  When the TestDateTimeAdjuster is set,
     * time might not increment as expected and depends on the implementation of the TestDateTimeAdjuster.
     */
    fun setTestDateTimeAdjuster(testDateTimeAdjuster: TestDateTimeAdjuster?) {
        this.testDateTimeAdjuster = testDateTimeAdjuster
    }

    /**
     * Interface for creating a test class that can only change time in a test environment.
     *
     * Attempting to implement this in production should be a red flag and easily caught by code review.
     */
    interface TestDateTimeAdjuster {
        fun currentTestMillis(): Long
    }
}