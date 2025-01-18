package com.loggily.loggily.router

import com.loggily.loggily.service.DashboardService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

private const val SEARCH_TEXT_PARAM = "contains"

@Configuration(proxyBeanMethods = false)
class DashboardRouter(private val dashboardService: DashboardService) {

    @Bean
    fun dashboardRoute() = coRouter {
        val dashboardPrefixPattern = "/api/dashboard"

        GET("$dashboardPrefixPattern/readable-logs").and(accept(MediaType.APPLICATION_NDJSON)).invoke {
            ServerResponse.ok().bodyAndAwait(dashboardService.getReadableLogs())
        }
        GET("$dashboardPrefixPattern/environments/name")
            .and(accept(MediaType.APPLICATION_JSON))
            .and(queryParam(SEARCH_TEXT_PARAM) { it.isNotEmpty() })
            .invoke {
                it.queryParam(SEARCH_TEXT_PARAM).orElseThrow()
                    .let { searchText ->
                        ServerResponse.ok()
                            .bodyValueAndAwait(dashboardService.findEnvironmentNamesContains(searchText))
                    }
            }
    }

}