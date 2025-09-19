package org.lithylabs.kaptest.core

import kotlinx.serialization.Serializable

@Serializable
class Recording (
    val createDateTime: String,
    val testClass: String,
    val testMethod: String,
    val startCurrentMillis: Long,
    val eventMap: MutableMap<String, RecordableEvent>
)