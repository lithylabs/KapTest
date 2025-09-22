package org.lithylabs.kaptest.kke_example

import org.lithylabs.kaptest.exposed.*

/**
 * Schema manager is the business logic component for managing database schemas.  In this simple example,
 * the only action is listing databases.
 */
class SchemaMgr(
    val db: RecordingDatabase,
    val schemaRepository: SchemaRepository,
    val timeClient: TimeClient
) {
    /**
     * Lists all database schemas not including system schemas such as pg_catalog.
     */
    suspend fun listDatabases(): Pair<List<String>, DateTimeDto> {
        val dbs = db.transaction {
            schemaRepository.listDatabases()
        }
        val timeDto = timeClient.getDateTime()
        val filteredDbs = dbs.filter { !it.startsWith("pg_") }

        return filteredDbs to timeDto
    }
}