package org.lithylabs.kaptest.core

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

/**
 * Serializes a String containing json to a json object that can be easily read and formatted by a json reader.  The
 * main purpose of this serializer is to allow custom objects with custom serialization to work with
 * kotlinx.serialization.  This serializer allows the `RecordableEvent` to be written as a json with an embedded object
 * and prevent nested json parsing issues.
 */
object StringJsonToJsonSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("RawJson", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {

        val obj = JsonMapper.parse(JsonElement.serializer(), value)
        encoder.encodeSerializableValue(JsonElement.serializer(), obj)
    }

    override fun deserialize(decoder: Decoder): String {
        val obj = decoder.decodeSerializableValue(JsonElement.serializer())
        return obj.toString()
    }
}