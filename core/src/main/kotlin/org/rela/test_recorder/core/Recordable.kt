package org.rela.test_recorder.core

import kotlinx.serialization.*
import org.rela.test_recorder.TestRecorder
import kotlin.reflect.typeOf

/**
 * An interface with extension functions to allow object to participate in recording and playback.
 *
 * Adding this interface requires that the object has a `recorder` property that gets set during testing,
 * but is null during normal operation.  If the `recorder` is null, recording/playback operations are ignored.
 *
 * In addition to implementing the interface, each method that should be recorded must call the either
 * `recordable` or `recordableNullable` to record or playback the method.  For example:
 *
 * ```
 * class RecordableExample (
 *   override val recorder: TestRecorder? = null
 * ) : Recordable {
 *   fun someMethod() = recordable<Data> { ... }
 *   fun someMethod() = recordable { ... } // inferred
 *   fun someMethodNullType() = recordableNullable { ... }
 * }
 * ```
 * If the type can't be inferred, you will get this runtime error:
 *
 * `Serializer for class 'kotlin.Any' is not found.`
 *
 * In that case, you can specify the type explicitly:
 *
 * `fun someMethodNullType() = recordableNullable<Data> { return null }`
 *
 */
interface Recordable {
    val recorder: TestRecorder?

    fun <T : Any> recordable(serializer: KSerializer<T>, block: ()-> T): T {
        return recordableNullable(serializer, block)!!
    }

    fun <T : Any> recordableNullable(serializer: KSerializer<T>, block: ()-> T?): T? {
        val rec = recorder
        return if (rec != null) {
            val callName = findCallName()
            if (rec.record) { // recording test
                val resp = block()
                val json = if (resp != null) JsonMapper.json.encodeToString(serializer, resp) else null
                rec.recordEvent(callName, json)
                resp
            } else { //playing back test
                val json = rec.fetchPlaybackJson(callName)
                val resp = if (json != null) JsonMapper.json.decodeFromString(serializer, json) else null
                resp
            }
        } else {
            block()
        }
    }

    fun findCallName(): String {
        val stackTrace = Exception().stackTrace
        val stackIt = stackTrace.iterator()
        stackIt.next() //remove findCallName
        var methodElement = stackIt.next()
        while (methodElement.methodName.startsWith("recordable")) {
            methodElement = stackIt.next()
        }
        return "${methodElement.className}.${methodElement.methodName}()"
    }
}

inline fun <reified T : Any> Recordable.recordable(noinline block: ()-> T): T {
    val serializer = serializer<T>()
    return recordable(serializer, block)
}

inline fun <reified T : Any> Recordable.recordableNullable(noinline block: ()-> T?): T? {
    return recordableNullable<T>(serializer<T>(), block)
}



