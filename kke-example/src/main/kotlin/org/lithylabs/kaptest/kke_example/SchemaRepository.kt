package org.lithylabs.kaptest.kke_example

import org.jetbrains.exposed.sql.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.core.*

class SchemaRepository(
    override val recorder: Recorder? = null
) : Recordable {
    fun listDatabases() = recordable {
        SchemaUtils.listDatabases()
    }
}