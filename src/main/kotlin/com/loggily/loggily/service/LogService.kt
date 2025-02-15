package com.loggily.loggily.service

import com.loggily.loggily.repository.Log
import com.loggily.loggily.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

private const val DEFAULT_LOGS_PAGE_SIZE = 50

@Service
class LogService(private val repository: LogRepository) {
    fun saveLogs(logs: List<Log>) = repository.saveAll(logs)

    fun getLogs() = repository.findAll()

    fun findEnvironmentNamesContains(searchText: String) = repository.findEnvironmentNames("%$searchText%")

    fun findApplicationNamesByEnvironment(environmentName: String) =
        repository.findApplicationNamesByEnvironment(environmentName)

    fun findHostNamesByEnvironmentAndApplication(environmentName: String, applicationName: String) = repository
        .findHostNamesByEnvironmentAndApplication(environmentName, applicationName)

    fun findLogs(
        environmentName: String,
        applicationName: String,
        hostName: String?,
        offset: Long?,
        limit: Int?
    ): Flow<Log> {
        return if (hostName != null) {
            repository.findByEnvironmentAndApplicationAndHost(
                environmentName,
                applicationName,
                hostName,
                limit = limit ?: DEFAULT_LOGS_PAGE_SIZE,
                offset = offset ?: 0,
            )
        } else {
            repository.findByEnvironmentAndApplication(
                environmentName,
                applicationName,
                limit = limit ?: DEFAULT_LOGS_PAGE_SIZE,
                offset = offset ?: 0,
            )
        }
    }
}