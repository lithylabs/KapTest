package org.lithylabs.kaptest.core

import org.lithylabs.kaptest.*
import java.time.*

/**
 * Test utility class that adjusts the DateTimeCreator to return a specific time for testing.
 *
 * @See DateTimeCreator
 */
object TestDateTimeCreator: DateTimeCreator.TestDateTimeAdjuster, AdjustableDateTime {
    /**
     * Millis that the test time was set to.
     */
    private var testCurrentMillis: Long? = null

    /**
     * Millis that the test time was updated.  Used to adjust the test time as time passes.
     */
    private var timeUpdated: Long = System.currentTimeMillis()

    /**
     * If true, when the test time is set, it will be adjusted as time passes. If false,
     * the test time will remain fixed until it is manually adjusted.
     *
     * This should only be set to false if a very specific time is needed for a test.
     */
    var adjustTestTimeAsTimePasses: Boolean = true

    override fun currentTestMillis(): Long {
        var millis = testCurrentMillis ?: System.currentTimeMillis()
        if (adjustTestTimeAsTimePasses) {
            val milliAdjust = System.currentTimeMillis() - timeUpdated
            millis += milliAdjust
        }
        return millis
    }

    override fun setCurrentTestMillis(millis: Long) {
        testCurrentMillis = millis
        timeUpdated = System.currentTimeMillis()
        DateTimeCreator.setTestDateTimeAdjuster(this)
    }

    fun resetToActualTime() {
        testCurrentMillis = null
        DateTimeCreator.setTestDateTimeAdjuster(null)
    }

    /**
     * Adjusts the test time by the number of milliseconds either up or down based on
     * positive or negative.
     */
    fun adjustMillis(millis: Long) {
        val currentMillis = currentTestMillis() + millis
        testCurrentMillis = currentMillis
        timeUpdated = System.currentTimeMillis()
    }

    /**
     * Adjusts the test time by the number of seconds either up or down based on
     * positive or negative.
     */
    fun adjustSeconds(seconds: Long) {
         adjustMillis(seconds * 1000)
    }

    /**
     * Adjusts the test time by the number of minutes either up or down based on
     * positive or negative.
     */
    fun adjustMinutes(minutes: Long) {
        adjustMillis(minutes * 60 * 1000)
    }

    /**
     * Adjusts the test time by the number of hours either up or down based on
     * positive or negative.
     */
    fun adjustHours(hours: Long) {
        adjustMillis(hours * 60 * 60 * 1000)
    }

    /**
     * Adjusts the test time by UTC standards which has no time zone.
     */
    fun adjustDaysUTC(days: Long) {
        adjustDaysByZoneId(days, ZoneId.of("UTC"))
    }

    /**
     * Adjusts the test time by the number of days taking into account Daylight savings in
     * the current system timezone.
     */
    fun adjustDaysLocal(days: Long) {
        adjustDaysByZoneId(days, ZoneId.systemDefault())
    }

    /**
     * Adjusts the test time by the number of days taking into account Daylight savings in
     * of the passed timezone.
     */
    fun adjustDaysByZoneId(days: Long, zoneId: ZoneId) {
        val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(currentTestMillis()), zoneId)
        val adjusted = dateTime.plus(Duration.ofDays(days))
        setCurrentTestMillis(adjusted.toInstant().toEpochMilli())
    }

    /**
     * Gets the current test time as an Instant.
     */
    fun getCurrentTestInstant(): Instant {
        return Instant.ofEpochMilli(currentTestMillis())
    }

    /**
     * Sets the current test time to the Epoch millis of the passed Instant.
     */
    fun setCurrentTestInstant(instant: Instant) {
        setCurrentTestMillis(instant.toEpochMilli())
    }
}