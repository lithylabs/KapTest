package org.lithylabs.kaptest.core

import kotlinx.serialization.Serializable

@Serializable
class RecordableEvent (
    val id: String,
    @Serializable(with = StringJsonToJsonSerializer::class)
    val json: String?
)