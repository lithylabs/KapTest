package org.lithylabs.kaptest.exposed

import org.lithylabs.kaptest.*
import org.testcontainers.containers.*

object TcExposedDatabase {
    fun start(recorder: Recorder? = null): RecordingDatabase {
        return if (recorder == null || recorder.isRecording) {
            val container = PostgreSQLContainer("postgres:16-alpine")
            container.start()

            PooledDatabase(
                driver = "org.postgresql.Driver",
                jdbcUrl = container.jdbcUrl,
                dbUser = container.username,
                dbPassword = container.password,
                recorder = recorder
            )
        } else {
            RecordingDatabase(null, recorder)
        }
    }

}