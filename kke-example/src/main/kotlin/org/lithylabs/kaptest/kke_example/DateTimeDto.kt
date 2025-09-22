package org.lithylabs.kaptest.kke_example

import kotlinx.serialization.*

@Serializable
data class DateTimeDto(
    val timezone: String,
    val formatted: String,
    val timestamp: Long,
    val weekDay: Int,
    val day: Int,
    val month: Int,
    val year: Int,
    val hour: Int,
    val minute: Int,
)
