package com.loggily.loggily.router

import com.loggily.loggily.service.DashboardService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

private const val ENVIRONMENT_NAME_PARAM = "environmentName"
private const val APPLICATION_NAME_PARAM = "applicationName"

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
        GET("$dashboardPrefixPattern/applications/name")
            .and(accept(MediaType.APPLICATION_JSON))
            .and(queryParam(ENVIRONMENT_NAME_PARAM) { it.isNotEmpty() })
            .invoke {
                val environmentName = it.queryParam(ENVIRONMENT_NAME_PARAM)
                    .orElseThrow { IllegalArgumentException("environmentName is required") }
                ServerResponse.ok()
                    .bodyValueAndAwait(dashboardService.findApplicationNamesByEnvironment(environmentName))
            }
        GET("$dashboardPrefixPattern/hosts/name")
            .and(accept(MediaType.APPLICATION_JSON))
            .and(queryParam(ENVIRONMENT_NAME_PARAM) { it.isNotEmpty() })
            .and(queryParam(APPLICATION_NAME_PARAM) { it.isNotEmpty() })
            .invoke {
                val environmentName = it.queryParam(ENVIRONMENT_NAME_PARAM).orElseThrow()
                val applicationName = it.queryParam(APPLICATION_NAME_PARAM).orElseThrow()
                ServerResponse.ok()
                    .bodyValueAndAwait(
                        dashboardService.findHostNamesByEnvironmentAndApplication(
                            environmentName,
                            applicationName
                        )
                    )
            }
    }

}