package org.rela.test_recorder.core

import org.rela.test_recorder.*

fun recordableTest(record: Boolean = false, block: (TestRecorder) -> Unit) {
    val config = TestRecorderConfig(true).apply {
        ignoreTestClasses.add("org.rela.test_recorder.core.RecordableFunctionsKt")
    }
    val recorder = TestRecorder(config, record)

    block(recorder)
}

fun recordableTest(config: TestRecorderConfig, block: (TestRecorder) -> Unit) {
    config.ignoreTestClasses.add("org.rela.test_recorder.core.RecordableFunctionsKt")
    val recorder = TestRecorder(config, config.record)

    block(recorder)
}