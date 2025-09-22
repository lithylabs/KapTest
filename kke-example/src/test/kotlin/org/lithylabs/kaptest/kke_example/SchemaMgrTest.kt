package org.lithylabs.kaptest.kke_example

import io.kotest.matchers.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.kodein.di.*
import org.lithylabs.kaptest.exposed.*
import org.lithylabs.kaptest.kodein.*

class SchemaMgrTest {
    val record = true

    @Test
    fun createKodein() {
        val kodein = ExampleKodein.createDI()
        assertNotNull(kodein)

        val repo by kodein.instance<SchemaRepository>()
        repo.recorder shouldBe null
    }

    @Test
    fun createKodein2() = KodeinRecorder.recordableKodeinTest(ExampleKodein.createDI(), record) { kodein ->
        assertNotNull(kodein)

        val db by kodein.instance<RecordingDatabase>()
        db.recorder shouldNotBe null

        val repo by kodein.instance<SchemaRepository>()
        repo.recorder shouldNotBe null
    }

    @Test
    fun listDatabases() = KodeinRecorder.recordableKodeinTest(ExampleKodein.createDI(), record) { kodein ->
        val schemaMgr by kodein.instance<SchemaMgr>()

        val (databases, timeDto) = schemaMgr.listDatabases()
        assertTrue(databases.isNotEmpty())
    }

}