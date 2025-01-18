package com.loggily.loggily.rest

import com.loggily.loggily.configuration.LoggilyPropertyConfig
import com.loggily.loggily.repository.Log
import com.loggily.loggily.repository.StructuredLog
import java.time.Instant

fun OtelPayload.toLogs(config: LoggilyPropertyConfig): List<Log> {
    return resourceLogs.flatMap { resourceLog ->
        resourceLog.scopeLogs.flatMap { scopeLog ->
            val scope = scopeLog.scope?.name
            scopeLog.logRecords.map { logRecord ->
                val attributes = combineAttributes(
                    resourceLog.resource.attributes ?: listOf(),
                    logRecord.attributes ?: listOf()
                )
                val logOrigin = createLogOrigin(config, attributes)
                Log(
                    id = null,
                    timestamp = toInstant(getLogTimestamp(logRecord)),
                    environment = logOrigin.environment,
                    host = logOrigin.host,
                    application = logOrigin.application,
                    severityNumber = logRecord.severityNumber ?: 0,
                    traceId = logRecord.traceId,
                    structuredLog = StructuredLog(
                        logger = scope,
                        body = logRecord.body.stringValue,
                        severityText = logRecord.severityText,
                        spanId = logRecord.spanId,
                        attributes = attributes
                    )
                )
            }
        }
    }
}

fun Log.toReadableLog(): ReadableLog {
    return ReadableLog(
        id = id!!,
        timestamp = timestamp,
        environment = environment,
        host = host,
        application = application,
        severityNumber = severityNumber,
        traceId = traceId,
        structuredLog = structuredLog
    )
}

private fun combineAttributes(
    resourceAttributes: List<Attribute>,
    logAttributes: List<Attribute>
): Map<String, String?> {
    val resourceMap = resourceAttributes.associate { it.key to it.value.value }
    val scopeMap = logAttributes.associate { it.key to it.value.value }
    return resourceMap + scopeMap
}


private fun createLogOrigin(config: LoggilyPropertyConfig, attributes: Map<String, String?>): LogOriginWrapper {
    findAttributeValues(
        attributes,
        listOf(config.logOrigin.environmentKey, config.logOrigin.hostKey, config.logOrigin.applicationKey)
    ).let {
        return LogOriginWrapper(
            it[config.logOrigin.environmentKey],
            it[config.logOrigin.hostKey],
            it[config.logOrigin.applicationKey]
        )
    }
}

private fun findAttributeValues(attributes: Map<String, String?>, keys: List<String>): Map<String, String?> =
    attributes.entries.filter { it.key in keys }.associate { it.key to it.value }

private fun getLogTimestamp(logRecord: LogRecord) = logRecord.timeUnixNano ?: logRecord.observedTimeUnixNano

private fun toInstant(epochNanoSeconds: Long): Instant =
    Instant.ofEpochSecond(epochNanoSeconds / 1_000_000_000, epochNanoSeconds % 1_000_000_000)

private data class LogOriginWrapper(val environment: String?, val host: String?, val application: String?)