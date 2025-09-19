package org.lithylabs.kaptest.kodein

import io.ktor.client.*
import kotlinx.coroutines.*
import org.kodein.di.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.core.*
import org.lithylabs.kaptest.exposed.*
import org.lithylabs.kaptest.ktor.*

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
            bindSingleton<ExposedDatabase>(overrides = true) {
                val existingDb: ExposedDatabase by di.instance<ExposedDatabase>()
                RecordingExposedDatabase(existingDb, recorder)
            }
            bindSingleton<HttpClient>(overrides = true) {
                val existingClient: HttpClient by di.instance<HttpClient>()
                KtorRecordableJsonClient(existingClient, recorder)
            }
        }
    }
}