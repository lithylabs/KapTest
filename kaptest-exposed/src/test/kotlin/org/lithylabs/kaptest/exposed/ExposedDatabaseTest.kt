package org.lithylabs.kaptest.exposed

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.lithylabs.kaptest.core.TestRecorder
import org.lithylabs.kaptest.core.TestRecorderConfig
import org.lithylabs.kaptest.core.recordableTest


class ExposedDatabaseTest {
    val recordConfig = TestRecorderConfig(record = false)


    @Test
    fun `postgres database test container`() = recordableTest(recordConfig) { recorder ->
        val db = TcExposedDatabase.start(recorder)

        if (recorder.isRecording) {
            db.dataSource shouldNotBe null
        } else {
            db.dataSource shouldBe null
        }

        val repo = SchemaRepository(db, recorder)
        val databases = db.transaction {
            repo.listDatabases()
        }

        databases.size shouldBeGreaterThan 0
    }

    @Test
    fun `test no database error`() = runBlocking<Unit> {
        val recorderTrue = TestRecorder(true)
        recorderTrue.recordingPath shouldBe "src/test/resources/recordings/org/lithylabs/kaptest/exposed/ExposedDatabaseTest/test no database error.json"
        val recorderFalse = TestRecorder(false)
        recorderFalse.recordingPath shouldBe "src/test/resources/recordings/org/lithylabs/kaptest/exposed/ExposedDatabaseTest/test no database error.json"

        // Don't start the database
        val db = TcExposedDatabase.start(recorderFalse)

        val repo = SchemaRepository(db, recorderTrue)
        shouldThrow<IllegalStateException> {
            db.transaction {
                repo.listDatabases()
            }
        }

    }

}