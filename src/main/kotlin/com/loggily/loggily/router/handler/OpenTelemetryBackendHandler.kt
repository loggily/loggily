package com.loggily.loggily.router.handler

import com.loggily.loggily.configuration.LoggilyPropertyConfig
import com.loggily.loggily.rest.OtelPayload
import com.loggily.loggily.rest.toLogs
import com.loggily.loggily.service.LogService
import kotlinx.coroutines.flow.collect
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class OpenTelemetryBackendHandler(
    private val config: LoggilyPropertyConfig,
    private val logService: LogService
) {

    suspend fun saveLogs(request: ServerRequest): ServerResponse {
        val payload = request.awaitBody(OtelPayload::class)
        logService.saveLogs(payload.toLogs(config)).collect()
        return ServerResponse.status(HttpStatus.CREATED).buildAndAwait()
    }

    suspend fun getLogs(request: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyAndAwait(logService.getLogs())
    }
}