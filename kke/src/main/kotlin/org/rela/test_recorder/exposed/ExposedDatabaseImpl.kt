package org.rela.test_recorder.exposed

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import javax.sql.*
import org.jetbrains.exposed.sql.transactions.transaction as exposedTransaction

open class ExposedDatabaseImpl(
    val jdbcUrl: String,
    val dbUser: String,
    val dbPassword: String,
    val driver: String = "org.postgresql.Driver",
    val maxPoolSize: Int = 10,
) : ExposedDatabase {

    val dataSource: DataSource
    val db: Database

    init {
        val url = jdbcUrl
        val config = HikariConfig().apply {
            jdbcUrl = url
            driverClassName = driver
            username = dbUser
            password = dbPassword
            maximumPoolSize = maxPoolSize
            isReadOnly = false
            transactionIsolation = "TRANSACTION_SERIALIZABLE"
        }
        dataSource = HikariDataSource(config)
        db = Database.connect(dataSource)
    }

    override suspend fun <T> transaction(
            transactionIsolation: Int?,
            context: CoroutineDispatcher,
            statement: TransactionInterface.() -> T
    ): T = withContext(context) {
        val isolation = transactionIsolation ?: db.transactionManager.defaultIsolationLevel

        exposedTransaction(isolation, false, db, statement)
    }

    override suspend fun <T> transactionAsync(
            transactionIsolation: Int?,
            context: CoroutineDispatcher,
            statement: suspend TransactionInterface.() -> T
    ): Deferred<T> {
        return suspendedTransactionAsync(context, db, transactionIsolation, statement = statement)
    }
}