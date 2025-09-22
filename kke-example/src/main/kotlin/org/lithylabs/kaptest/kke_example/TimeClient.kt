package org.lithylabs.kaptest.kke_example

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TimeClient(
    val client: HttpClient
) {
    suspend fun getDateTime(): DateTimeDto {
        val result = client.get("https://tools.aimylogic.com/api/now")
        // val text = result.bodyAsText()
        // println(text)
        return result.body<DateTimeDto>()


    }
}