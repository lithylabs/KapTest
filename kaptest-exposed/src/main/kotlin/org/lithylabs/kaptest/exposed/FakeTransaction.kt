package org.lithylabs.kaptest.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.api.*
import org.jetbrains.exposed.sql.transactions.*


class FakeTransaction: TransactionInterface {
    override val connection: ExposedConnection<*>
        get() = throw IllegalStateException("Not implemented for recordings")
    override val db: Database
        get() = throw IllegalStateException("Not implemented for recordings")
    override val outerTransaction: Transaction?
        get() = throw IllegalStateException("Not implemented for recordings")
    override val transactionIsolation: Int
        get() = throw IllegalStateException("Not implemented for recordings")

    override fun close() {
        throw IllegalStateException("Not implemented for recordings")
    }

    override fun commit() {
        throw IllegalStateException("Not implemented for recordings")
    }

    override fun rollback() {
        throw IllegalStateException("Not implemented for recordings")
    }

    override val readOnly: Boolean
        get() = throw IllegalStateException("Not implemented for recordings")
}
