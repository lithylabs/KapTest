package org.lithylabs.kaptest.kodein

import kotlinx.coroutines.*
import org.kodein.di.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.core.*
import org.lithylabs.kaptest.exposed.*

object KodeinRecorder {

    fun recordableKodeinTest(
        kodein: DI,
        record: Boolean = false,
        block: suspend (DI) -> Unit
    ) {
        val config = TestRecorderConfig().apply {
            ignoreTestClasses.add(KodeinRecorder::class.qualifiedName!!)
        }
        val testRecorder = TestRecorder(config, record)

        val testDi = wrap(kodein, testRecorder)

        runBlocking {
            block(testDi)
        }
    }

    fun recordableKodeinTest(
        kodein: DI,
        config: TestRecorderConfig,
        block: suspend (DI) -> Unit
    ) {
        config.ignoreTestClasses.add(KodeinRecorder::class.qualifiedName!!)
        val testRecorder = TestRecorder(config, config.record)

        val testDi = wrap(kodein, testRecorder)

        runBlocking {
            block(testDi)
        }
    }

    fun wrap(di: DI, recorder: Recorder): DI {
        return DI(allowSilentOverride = true) {
            bindSingleton<Recorder> { recorder }

            extend(di, copy = Copy.All)
            bindSingleton<RecordingDatabase>(overrides = true) {
                val existingDb: RecordingDatabase by di.instance<RecordingDatabase>()
                RecordingDatabase(existingDb.dataSource, recorder)
            }
        }
    }
}