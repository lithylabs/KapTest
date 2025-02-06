package org.rela.test_recorder.core

import kotlinx.serialization.Serializable

class EventRecord(
    val id: String,
    val type: String,
    val data: Serializable
)