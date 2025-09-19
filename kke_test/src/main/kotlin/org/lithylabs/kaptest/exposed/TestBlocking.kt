package org.lithylabs.kaptest.exposed

import kotlinx.coroutines.*

fun testBlocking(block: suspend () -> Unit) {
    runBlocking {
        block()
    }
}