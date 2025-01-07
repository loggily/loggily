package com.loggily.loggily.rest

import com.loggily.loggily.repository.Log
import com.loggily.loggily.repository.StructuredLog
import java.time.Instant

private const val NAMESPACE_ATTRIBUTE_KEY = "k8s.namespace.name"
private const val POD_ATTRIBUTE_KEY = "k8s.pod.name"
private const val CONTAINER_ATTRIBUTE_KEY = "k8s.container.name"

fun OtelPayload.toLogs(): List<Log> {
    return resourceLogs.flatMap { resourceLog ->
        resourceLog.scopeLogs.flatMap { scopeLog ->
            val scope = scopeLog.scope?.name
            scopeLog.logRecords.map { logRecord ->
                val attributes = combineAttributes(
                    resourceLog.resource.attributes ?: listOf(),
                    logRecord.attributes ?: listOf()
                )
                val logOrigin = createLogOrigin(attributes)
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


private fun createLogOrigin(attributes: Map<String, String?>): LogOrigin {
    findAttributeValues(attributes, listOf(NAMESPACE_ATTRIBUTE_KEY, POD_ATTRIBUTE_KEY, CONTAINER_ATTRIBUTE_KEY)).let {
        return LogOrigin(
            it[NAMESPACE_ATTRIBUTE_KEY],
            it[POD_ATTRIBUTE_KEY],
            it[CONTAINER_ATTRIBUTE_KEY]
        )
    }
}

private fun findAttributeValues(attributes: Map<String, String?>, keys: List<String>): Map<String, String?> =
    attributes.entries.filter { it.key in keys }.associate { it.key to it.value }

private fun getLogTimestamp(logRecord: LogRecord) = logRecord.timeUnixNano ?: logRecord.observedTimeUnixNano

private fun toInstant(epochNanoSeconds: Long): Instant =
    Instant.ofEpochSecond(epochNanoSeconds / 1_000_000_000, epochNanoSeconds % 1_000_000_000)

private data class LogOrigin(val environment: String?, val host: String?, val application: String?)