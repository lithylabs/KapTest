package org.rela.test_recorder.exposed

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionInterface
import org.rela.test_recorder.*


class RecordingExposedDatabase(
    val exposedDatabase: ExposedDatabase?,
    val recorder: TestRecorder,
): ExposedDatabase {

    /**
     * Creates a new transaction if recording is enabled, otherwise creates a new TestTransaction
     * For more details, see [ExposedDatabase.transaction]
     *
     * @throws NullPointerException if exposedDatabase is null and recording is enabled
     */
    override suspend fun <T> transaction(
        transactionIsolation: Int?,
        context: CoroutineDispatcher,
        statement: TransactionInterface.() -> T
    ): T {
        return if (recorder.record) {
            exposedDatabase!!.transaction(transactionIsolation, context, statement)
        } else {
            TestTransaction().statement()
        }
    }

    /**
     * Creates a new async transaction if recording is enabled, otherwise creates a new TestTransaction.
     * For more details, see [ExposedDatabase.transactionAsync]
     *
     * @throws NullPointerException if exposedDatabase is null and recording is enabled
     */
    override suspend fun <T> transactionAsync(
            transactionIsolation: Int?,
            context: CoroutineDispatcher,
            statement: suspend TransactionInterface.() -> T
    ): Deferred<T> {
        return if (recorder.record) {
            exposedDatabase!!.transactionAsync(transactionIsolation, context, statement)
        } else {
            coroutineScope() {
                async { TestTransaction().statement() }
            }
        }
    }
}