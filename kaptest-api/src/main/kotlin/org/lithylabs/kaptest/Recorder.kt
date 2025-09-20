package org.lithylabs.kaptest

interface Recorder {
    val isRecording: Boolean
//    fun <T : Any> handleRecordOrPlayback(callName: String, serializer: KSerializer<T>, block: ()-> T?): T?

    fun recordEvent(callName: String, json: String?)

    fun fetchPlaybackJson(callName: String): String?
}