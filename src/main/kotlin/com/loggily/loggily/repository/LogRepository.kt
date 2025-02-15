package com.loggily.loggily.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : CoroutineCrudRepository<Log, Long> {

    @Query(
        """
        SELECT DISTINCT environment 
        FROM logs 
        WHERE environment LIKE $1 
        ORDER BY environment ASC
        """
    )
    fun findEnvironmentNames(searchText: String): Flow<String>

    @Query(
        """
        SELECT DISTINCT application 
        FROM logs 
        WHERE environment = $1 
        ORDER BY application ASC
        """
    )
    fun findApplicationNamesByEnvironment(environmentName: String): Flow<String>

    @Query(
        """
        SELECT DISTINCT host 
        FROM logs 
        WHERE environment = $1 AND application = $2 
        ORDER BY host ASC
        """
    )
    fun findHostNamesByEnvironmentAndApplication(environmentName: String, applicationName: String): Flow<String>

    @Query(
        """
        SELECT * 
        FROM logs 
        WHERE environment = $1 AND application = $2
        ORDER BY timestamp ASC
        LIMIT $3
        OFFSET $4
        """
    )
    fun findByEnvironmentAndApplication(
        environmentName: String,
        applicationName: String,
        limit: Int,
        offset: Long,
    ): Flow<Log>

    @Query(
        """
        SELECT * 
        FROM logs 
        WHERE environment = $1 AND application = $2 AND host = $3
        ORDER BY timestamp ASC
        LIMIT $4
        OFFSET $5
        """
    )
    fun findByEnvironmentAndApplicationAndHost(
        environmentName: String,
        applicationName: String,
        host: String,
        limit: Int?,
        offset: Long
    ): Flow<Log>
}