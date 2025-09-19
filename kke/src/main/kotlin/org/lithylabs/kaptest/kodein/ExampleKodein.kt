package org.lithylabs.kaptest.kodein

import io.ktor.client.*
import org.kodein.di.*
import org.lithylabs.kaptest.exposed.*
import org.testcontainers.containers.*

object ExampleKodein {
    fun createDI() = DI {

        bindSingleton<HttpClient> {
            HttpClient()
        }

        bindSingleton<ExposedDatabase> {
            val container = PostgreSQLContainer("postgres:16-alpine")
            container.start()
            ExposedDatabaseImpl(
                driver = "org.postgresql.Driver",
                jdbcUrl = container.jdbcUrl,
                dbUser = container.username,
                dbPassword = container.password
            )
        }

        bindSingleton { SchemaRepository(instance(), instance()) }
    }
}