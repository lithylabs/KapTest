package org.lithylabs.kaptest.kke_example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import org.kodein.di.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.exposed.*
import org.lithylabs.kaptest.ktor.*
import org.testcontainers.containers.*

/**
 * Kodein module defining all the dependencies required by the application.
 */
object ExampleKodein {
    fun createDI() = DI.Companion {

        // Ktor HttpClient
        bindSingleton<HttpClient> {
            println("Creating HttpClient...")
            val client = RecordableHttpClient(CIO, instanceOrNull<Recorder>()) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true  // in case API adds extra fields
                    })
                }
            }
            println("HTTPClient:" + client.pluginOrNull(ContentNegotiation))
            client
        }
        bindSingleton<TimeClient> {
            TimeClient(instance())
        }

        // Exposed database and repositories
        bindSingleton<RecordingDatabase> {
            val container = PostgreSQLContainer("postgres:16-alpine")
            container.start()
            PooledDatabase(
                driver = "org.postgresql.Driver",
                jdbcUrl = container.jdbcUrl,
                dbUser = container.username,
                dbPassword = container.password,
                recorder = instanceOrNull()
            )
        }
        bindSingleton { SchemaRepository(instanceOrNull()) }

        // Managers
        bindSingleton { SchemaMgr(instance(), instance(), instance()) }
    }
}