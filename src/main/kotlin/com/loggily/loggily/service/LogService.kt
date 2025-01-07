package com.loggily.loggily.service

import com.loggily.loggily.repository.Log
import com.loggily.loggily.repository.LogRepository
import org.springframework.stereotype.Service

@Service
class LogService(private val repository: LogRepository) {
    fun saveLogs(logs: List<Log>) = repository.saveAll(logs)

    fun getLogs() = repository.findAll()
}