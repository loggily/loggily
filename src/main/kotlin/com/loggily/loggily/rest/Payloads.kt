package com.loggily.loggily.rest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.loggily.loggily.repository.StructuredLog
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
data class OtelPayload(val resourceLogs: List<ResourceLog>)
data class ResourceLog(val resource: Resource, val scopeLogs: List<ScopeLog>, val schemaUrl: String)
data class Resource(val attributes: List<Attribute>?)
data class ScopeLog(val scope: Scope?, val logRecords: List<LogRecord>)
data class Scope(val name: String?)
data class LogRecord(
    val timeUnixNano: Long?,
    val observedTimeUnixNano: Long,
    val severityNumber: Int?,
    val severityText: String?,
    val body: SimpleValue,
    val attributes: List<Attribute>? = null,
    val traceId: String?,
    val spanId: String?,
    val flags: String?
)

data class Attribute(val key: String, val value: SimpleValue)
data class SimpleValue(val stringValue: String?, val intValue: Int?) {
    val value: String?
        get() = stringValue ?: intValue?.toString()

}

data class ReadableLog(
    val id: Long,
    val timestamp: Instant,
    val environment: String?,
    val host: String?,
    val application: String?,
    val severityNumber: Int,
    val traceId: String?,
    val structuredLog: StructuredLog
)