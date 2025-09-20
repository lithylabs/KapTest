package org.lithylabs.kaptest.exposed

import org.jetbrains.exposed.sql.SchemaUtils
import org.lithylabs.kaptest.Recorder
import org.lithylabs.kaptest.core.Recordable
import org.lithylabs.kaptest.core.recordable

class SchemaRepository(
    val db: RecordingDatabase,
    override val recorder: Recorder
): Recordable {
    fun listDatabases() = recordable {
        SchemaUtils.listDatabases()
    }

    fun staticCall() = recordable {
        5
    }
}