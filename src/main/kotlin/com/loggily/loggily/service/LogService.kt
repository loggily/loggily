package com.loggily.loggily.service

import com.loggily.loggily.repository.Log
import com.loggily.loggily.repository.LogQueryRepository
import com.loggily.loggily.repository.LogRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class LogService(private val repository: LogRepository, private val logQueryRepository: LogQueryRepository) {
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
        return logQueryRepository.findLogs(environmentName, applicationName, hostName, limit, offset)
    }
}