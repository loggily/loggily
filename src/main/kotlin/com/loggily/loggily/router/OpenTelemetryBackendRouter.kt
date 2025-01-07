package com.loggily.loggily.router

import com.loggily.loggily.router.handler.OpenTelemetryBackendHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration(proxyBeanMethods = false)
class OpenTelemetryBackendRouter {

    @Bean
    fun logsRoute(handler: OpenTelemetryBackendHandler) = coRouter {
        POST("/v1/logs").and(contentType(MediaType.APPLICATION_JSON)).invoke { handler.saveLogs(it) }
        GET("/v1/logs").and(accept(MediaType.APPLICATION_NDJSON)).invoke { handler.getLogs(it) }
    }
}