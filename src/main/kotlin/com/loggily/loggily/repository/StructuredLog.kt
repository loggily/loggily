package com.loggily.loggily.repository

data class StructuredLog(
    val logger: String?,
    val body: String?,
    val severityText: String?,
    val spanId: String?,
    val attributes: Map<String, String?>
)
