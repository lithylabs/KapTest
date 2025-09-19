package org.lithylabs.kaptest.exposed

import io.kotest.matchers.longs.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.lithylabs.kaptest.*
import kotlin.system.*

class RecordingExposedDatabaseTest {

    @Test
    fun `recordable database test`() = testBlocking {
        val recorder = MockTestRecorder()
        recorder.readyTest(record = true)

        val exposedDatabase = TcExposedDatabase.start(recorder = recorder)
        val repository = SchemaRepository(
            exposedDatabase = exposedDatabase,
            recorder = recorder
        )

        exposedDatabase.transaction {
            repository.listDatabases()
        }

        assertTrue(recorder.recordCalled)
        assertFalse(recorder.playbackCalled)

        recorder.readyTest(record = false, json = """["databases"]""")

        exposedDatabase.transaction {
            repository.listDatabases()
        }

        assertFalse(recorder.recordCalled)
        assertTrue(recorder.playbackCalled)

    }

    @Test
    fun `ensure db container not spun up when not recording`() = testBlocking {
        val recorder = MockTestRecorder()
        recorder.readyTest(record = false, json = """["databases"]""")

        val exeTime = measureTimeMillis {
            val exposedDatabase = TcExposedDatabase.start(recorder = recorder)
            val repository = SchemaRepository(
                exposedDatabase = exposedDatabase,
                recorder = recorder
            )

            exposedDatabase.transaction {
                repository.listDatabases()
            }
        }
        println("Execution time: $exeTime")
        exeTime shouldBeLessThan  1000
    }



}