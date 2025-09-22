package org.lithylabs.kaptest.kke_example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.kodein.di.*
import org.lithylabs.kaptest.kke_example.ExampleKodein.createDI
import org.lithylabs.kaptest.kodein.KodeinRecorder.recordableKodeinTest

class TimeClientTest {
    val record = true

    @Test
    fun getDateTime() = recordableKodeinTest(createDI(), record) { kodein ->
        val client by kodein.instance<TimeClient>()

        val result = client.getDateTime()
        assertNotNull(result)
        println(result)
    }

    @Test
    fun fetchDateTime() = recordableKodeinTest(createDI(), record) { kodein ->

        val kodeinProd = createDI()
        val client by kodeinProd.instance<HttpClient>()
        val client2 by kodein.instance<HttpClient>()

        val resp = client.get("https://tools.aimylogic.com/api/now") {
            // accept(io.ktor.http.ContentType.Application.Json)
        }
        val dto = resp.body<DateTimeDto>()

        println(client2.pluginOrNull(ContentNegotiation))
        val dto2 = client2.get("https://tools.aimylogic.com/api/now").body<DateTimeDto>()



        println(dto)
        client.close()
    }

}