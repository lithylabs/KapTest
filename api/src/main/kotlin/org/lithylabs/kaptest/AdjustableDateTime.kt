package org.lithylabs.kaptest

interface AdjustableDateTime  {
    fun currentTestMillis(): Long
    fun setCurrentTestMillis(millis: Long)
}
