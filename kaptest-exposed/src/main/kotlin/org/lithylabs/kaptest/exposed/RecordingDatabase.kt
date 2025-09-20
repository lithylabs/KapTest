package org.lithylabs.kaptest.exposed

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.CompositeSqlLogger
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionInterface
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transactionManager
import org.lithylabs.kaptest.Recorder
import org.jetbrains.exposed.sql.transactions.transaction as exposedTransaction
import javax.sql.DataSource

open class RecordingDatabase(
    val dataSource: DataSource?,
    val recorder: Recorder?,
) {
    val db: Database? = if (dataSource != null && recorder?.isRecording ?: false) {
        Database.connect(dataSource)
    } else {
        null
    }

    /**
     * Creates a new transaction with the internal database connected to the passed DataSource and then
     * calls the provided statement.
     *
     * If not recording, database connection is never created, and [FakeTransaction] is used.  Any database
     * operations must be wrapped in [recordable] to throw IllegalStateExceptions.
     * Todo: fix recorable
     *
     * For more details, see [Exposed transaction][org.jetbrains.exposed.sql.transactions.transaction].
     *
     * @throws NullPointerException if exposedDatabase is null and recording is enabled
     */
    suspend fun <T> transaction(
        transactionIsolation: Int? = null,
        context: CoroutineDispatcher = Dispatchers.IO,
        statement: TransactionInterface.() -> T
    ): T = withContext(context) {
        val isolation = transactionIsolation ?: db.transactionManager.defaultIsolationLevel

        if (db != null) {
            exposedTransaction(isolation, false, db, statement)
        } else {
            FakeTransaction().statement()
        }
    }


    /**
     * TODO: Add documentation
     */
    suspend fun <T> transactionAsync(
        transactionIsolation: Int? = null,
        context: CoroutineDispatcher = Dispatchers.IO,
        statement: suspend TransactionInterface.() -> T
    ): Deferred<T> {
        return if (db != null) {
            suspendedTransactionAsync(context, db, transactionIsolation, statement = statement)
        } else {
            coroutineScope {
                async { FakeTransaction().statement() }
            }
        }
    }

    fun TransactionInterface.addLogger(vararg logger: SqlLogger): CompositeSqlLogger {
        this as Transaction
        return CompositeSqlLogger().apply {
            logger.forEach { this.addLogger(it) }
            registerInterceptor(this)
        }
    }
}

