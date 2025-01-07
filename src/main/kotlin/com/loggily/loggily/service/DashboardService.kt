package com.loggily.loggily.service

import com.loggily.loggily.rest.ReadableLog
import com.loggily.loggily.rest.toReadableLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class DashboardService(private val logService: LogService) {
    suspend fun getReadableLogs(): Flow<ReadableLog> = logService.getLogs().map {
        it.toReadableLog()
    }
}