package org.lithylabs.kaptest.exposed

import org.jetbrains.exposed.sql.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.core.*

class SchemaRepository(
    val exposedDatabase: ExposedDatabase,
    override val recorder: Recorder
): Recordable {
    fun listDatabases() = recordable {
        SchemaUtils.listDatabases()
    }
}