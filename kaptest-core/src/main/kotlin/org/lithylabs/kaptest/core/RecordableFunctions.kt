package org.lithylabs.kaptest.core

import kotlinx.coroutines.runBlocking

fun recordableTest(record: Boolean = false, block: (TestRecorder) -> Unit) {
    val config = TestRecorderConfig(true).apply {
        ignoreTestClasses.add("org.lithylabs.kaptest.core.RecordableFunctionsKt")
    }
    val recorder = TestRecorder(config, record)

    block(recorder)
}

fun recordableTest(config: TestRecorderConfig, block: suspend (recorder: TestRecorder) -> Unit) {
    config.ignoreTestClasses.add("org.lithylabs.kaptest.core.RecordableFunctionsKt")
    val recorder = TestRecorder(config, config.record)

    runBlocking {
        block(recorder)
    }
}