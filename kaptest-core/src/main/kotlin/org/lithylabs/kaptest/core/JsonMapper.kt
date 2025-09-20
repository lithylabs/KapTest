package org.lithylabs.kaptest.core

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

object JsonMapper {
    val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
    }
    val jsonPretty = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
    }

    fun <T> stringify(serializer: SerializationStrategy<T>, obj: T, prettyPrint: Boolean = false) = if (prettyPrint) {
        jsonPretty.encodeToString(serializer, obj)
    } else {
        json.encodeToString(serializer, obj)
    }

    fun <T> parse(deserializer: DeserializationStrategy<T>, string: String) = json.decodeFromString(deserializer, string)
}