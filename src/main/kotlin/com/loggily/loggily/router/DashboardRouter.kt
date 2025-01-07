package com.loggily.loggily.router

import com.loggily.loggily.service.DashboardService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration(proxyBeanMethods = false)
class DashboardRouter(private val dashboardService: DashboardService) {

    @Bean
    fun dashboardRoute() = coRouter {
        GET("/api/dashboard/readable-logs").and(accept(MediaType.APPLICATION_NDJSON)).invoke {
            ServerResponse.ok().bodyAndAwait(dashboardService.getReadableLogs())
        }
    }

}