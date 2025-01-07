package com.loggily.loggily.repository

import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("logs")
data class Log(
    val id: Long?,
    val timestamp: Instant,
    val environment: String?,
    val host: String?,
    val application: String?,
    val severityNumber: Int,
    val traceId: String?,
    val structuredLog: StructuredLog
)
