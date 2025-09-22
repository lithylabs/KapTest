package org.lithylabs.kaptest.kke_example

import com.zaxxer.hikari.*
import org.lithylabs.kaptest.*
import org.lithylabs.kaptest.exposed.*
import javax.sql.*

open class PooledDatabase(
    jdbcUrl: String,
    dbUser: String,
    dbPassword: String,
    driver: String = "org.postgresql.Driver",
    maxPoolSize: Int = 10,
    recorder: Recorder?
) : RecordingDatabase(
    createDataSource(jdbcUrl, dbUser, dbPassword, driver, maxPoolSize),
    recorder
) {
    companion object {
        /**
         * Create a HikariCP DataSource for the specified connection details.
         */
        fun createDataSource(
            url: String,
            dbUser: String,
            dbPassword: String,
            driver: String = "org.postgresql.Driver",
            maxPoolSize: Int = 10,
        ): DataSource {
            val config = HikariConfig().apply {
                jdbcUrl = url
                driverClassName = driver
                username = dbUser
                password = dbPassword
                maximumPoolSize = maxPoolSize
                isReadOnly = false
                transactionIsolation = "TRANSACTION_SERIALIZABLE"
            }
            return HikariDataSource(config)
        }
    }
}