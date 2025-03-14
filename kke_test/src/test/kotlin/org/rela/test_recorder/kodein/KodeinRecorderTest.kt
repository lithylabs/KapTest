package org.rela.test_recorder.kodein

import io.kotest.matchers.*
import io.kotest.matchers.string.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.kodein.di.*
import org.rela.test_recorder.*
import org.rela.test_recorder.core.*
import org.rela.test_recorder.exposed.*

class KodeinRecorderTest {
    val recorderConfig = TestRecorderConfig(record = false)

    @Test
    fun database() = KodeinRecorder.recordableKodeinTest(ExampleKodein.createDI(), recorderConfig) { di ->

        val recorder by di.instance<Recorder>()
        val testRecorder = recorder as TestRecorder
        testRecorder.recordingPath shouldBe "src/test/resources/recordings/org/rela/test_recorder/kodein/KodeinRecorderTest/database.json"


        val db by di.instance<ExposedDatabase>()
        val repo by di.instance<SchemaRepository>()

        val dbList = db.transaction {
            repo.listDatabases()
        }

        assertEquals(4, dbList.size)
    }

    @Test
    fun httpclient() = KodeinRecorder.recordableKodeinTest(ExampleKodein.createDI(), recorderConfig) { di ->
        val client by di.instance<HttpClient>()

        val result = client.get("https://tools.aimylogic.com/api/now")

        val body = result.bodyAsText()
        body shouldContain "14.03.2025 19:24"
        result.status shouldBe HttpStatusCode.OK
    }

}