package com.loggily.loggily.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : CoroutineCrudRepository<Log, Long> {

    @Query("SELECT DISTINCT environment FROM logs WHERE environment LIKE ?1")
    fun findEnvironmentNames(searchText: String): Flow<String>
}