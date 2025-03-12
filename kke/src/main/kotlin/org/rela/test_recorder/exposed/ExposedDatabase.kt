package org.rela.test_recorder.exposed

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionInterface
import org.rela.test_recorder.exposed.addLogger

interface ExposedDatabase {
    suspend fun <T> transaction(transactionIsolation: Int? = null,
                                context: CoroutineDispatcher = Dispatchers.IO,
                                statement: TransactionInterface.() -> T) : T

    suspend fun <T> transactionAsync(transactionIsolation: Int? = null,
                                     context: CoroutineDispatcher = Dispatchers.IO,
                                     statement: suspend TransactionInterface.() -> T) : Deferred<T>

}

fun TransactionInterface.addLogger(vararg logger: SqlLogger) : CompositeSqlLogger {
    this as Transaction
    return CompositeSqlLogger().apply {
        logger.forEach { this.addLogger(it) }
        registerInterceptor(this)
    }
}