package org.rela.test_recorder.exposed

import org.jetbrains.exposed.sql.*
import org.rela.test_recorder.*
import org.rela.test_recorder.core.*

class SchemaRepository(
    val exposedDatabase: ExposedDatabase,
    override val recorder: Recorder
): Recordable {
    fun listDatabases() = recordable {
        SchemaUtils.listDatabases()
    }
}