package org.rela.test_recorder.exposed

import org.rela.test_recorder.*
import org.testcontainers.containers.*

object TcExposedDatabase {
    fun start(recorder: TestRecorder? = null): ExposedDatabase {

        return if (recorder == null || recorder.record) {
            val container = PostgreSQLContainer("postgres:16-alpine")
            container.start()
            val db = ExposedDatabaseImpl(
                driver = "org.postgresql.Driver",
                jdbcUrl = container.jdbcUrl,
                dbUser = container.username,
                dbPassword = container.password
            )

            if (recorder != null) {
                RecordingExposedDatabase(db, recorder)
            } else {
                db
            }

        } else {
            RecordingExposedDatabase(null, recorder)
        }

    }

}