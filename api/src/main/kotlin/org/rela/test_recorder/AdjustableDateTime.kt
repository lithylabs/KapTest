package org.rela.test_recorder

interface AdjustableDateTime  {
    fun currentTestMillis(): Long
    fun setCurrentTestMillis(millis: Long)
}
