package org.rela.test_recorder.ktor

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
class KtorJsonClientEvent(
    val requestMethod: String,
    val requestUrl: String,
    val reqJson: String,
    val respStatus: Int,
    val respHeaders: Map<String, List<String>>,
    val respJson: JsonElement?
) {
}