package com.loggily.loggily.repository

import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.flow
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository


@Repository
class LogQueryRepository(private val template: R2dbcEntityTemplate) {

    fun findLogs(
        environmentName: String,
        applicationName: String,
        host: String?,
        limit: Int?,
        offset: Long?,
        sort: Sort = Sort.by("timestamp").ascending()
    ): Flow<Log> {

        val criteria =
            Criteria.from(
                Criteria.where("environment").`is`(environmentName),
                Criteria.where("application").`is`(applicationName)
            ).let { if (host != null) it.and(Criteria.where("host").`is`(host)) else it }

        val query = Query.query(criteria)
            .offset(offset ?: 0)
            .sort(sort)
            .let { if (limit != null) it.limit(limit) else it }

        return template.select(Log::class.java)
            .matching(query)
            .flow()
    }
}