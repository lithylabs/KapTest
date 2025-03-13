package org.rela.test_recorder.kodein

import org.kodein.di.*
import org.rela.test_recorder.exposed.*
import org.testcontainers.containers.*

object ExampleKodein {
    fun createDI() = DI {
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