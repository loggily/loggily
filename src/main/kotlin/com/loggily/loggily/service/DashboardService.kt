package com.loggily.loggily.service

import com.loggily.loggily.rest.ReadableLog
import com.loggily.loggily.rest.toReadableLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import org.springframework.stereotype.Service

@Service
class DashboardService(private val logService: LogService) {
    fun getReadableLogs(): Flow<ReadableLog> = logService.getLogs().map {
        it.toReadableLog()
    }

    suspend fun findEnvironmentNamesContains(searchText: String): List<String> =
        logService.findEnvironmentNamesContains(searchText)
            .toCollection(mutableListOf())

    suspend fun findApplicationNamesByEnvironment(environmentName: String): List<String> =
        logService.findApplicationNamesByEnvironment(environmentName)
            .toCollection(mutableListOf())

    suspend fun findHostNamesByEnvironmentAndApplication(
        environmentName: String,
        applicationName: String
    ): List<String> =
        logService.findHostNamesByEnvironmentAndApplication(environmentName, applicationName)
            .toCollection(mutableListOf())

    suspend fun findLogs(
        environmentName: String,
        applicationName: String,
        hostName: String?,
        offset: Long?,
        limit: Int?
    ): Flow<ReadableLog> =
        logService.findLogs(environmentName, applicationName, hostName, offset, limit).map {
            it.toReadableLog()
        }
}