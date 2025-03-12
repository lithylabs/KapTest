package org.rela.test_recorder.exposed

import kotlinx.coroutines.*

fun testBlocking(block: suspend () -> Unit) {
    runBlocking {
        block()
    }
}