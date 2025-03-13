package org.rela.test_recorder

interface Recorder {
    val record: Boolean
//    fun <T : Any> handleRecordOrPlayback(callName: String, serializer: KSerializer<T>, block: ()-> T?): T?

    fun recordEvent(callName: String, json: String?)

    fun fetchPlaybackJson(callName: String): String?
}