package org.rela.test_recorder.kodein

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.kodein.di.*
import org.rela.test_recorder.exposed.*

class KodeinRecorderTest {
    @Test
    fun simple() = testBlocking {
        val di = KodeinRecorder.wrap(ExampleKodein.createDI(), record = true)

        val db by di.instance<ExposedDatabase>()
        val repo by di.instance<SchemaRepository>()

        val dbList = db.transaction {
            repo.listDatabases()
        }

        assertEquals(1, dbList.size)
    }

}