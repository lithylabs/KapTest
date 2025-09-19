package org.lithylabs.kaptest.kodein

import io.kotest.matchers.*
import io.kotest.matchers.string.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.kodein.di.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.core.*
import org.lithylabs.kaptest.exposed.*

class KodeinRecorderTest {
    val recorderConfig = TestRecorderConfig()

    @Test
    fun database() = KodeinRecorder.recordableKodeinTest(ExampleKodein.createDI(), recorderConfig) { di ->

        val recorder by di.instance<Recorder>()
        val testRecorder = recorder as TestRecorder
        testRecorder.recordingPath shouldBe "src/test/resources/recordings/org/lithylabs/kaptest/kodein/KodeinRecorderTest/database.json"


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
        val time = JsonMapper.json.decodeFromString<DateTimeDto>(body)
        time.timezone shouldBe "UTC"
        result.status shouldBe HttpStatusCode.OK
    }

    @Serializable
    data class DateTimeDto(
        val timezone: String,
        val formatted: String,
        val timestamp: Long,
    )

}