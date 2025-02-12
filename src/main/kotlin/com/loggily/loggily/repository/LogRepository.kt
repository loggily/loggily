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
}