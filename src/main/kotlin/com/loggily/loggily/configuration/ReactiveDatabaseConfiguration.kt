package com.loggily.loggily.configuration

import com.loggily.loggily.repository.JsonToStructuredLogConverter
import com.loggily.loggily.repository.StructuredLogToJsonConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect

@Configuration(proxyBeanMethods = false)
class ReactiveDatabaseConfiguration {

    @Bean
    fun registerCustomizedPostgresR2dbcCustomConversions(): R2dbcCustomConversions {
        return R2dbcCustomConversions.of(
            PostgresDialect.INSTANCE, listOf(
                StructuredLogToJsonConverter(),
                JsonToStructuredLogConverter()
            )
        )
    }
}