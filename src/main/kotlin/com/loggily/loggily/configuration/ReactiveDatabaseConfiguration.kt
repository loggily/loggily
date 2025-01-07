package com.loggily.loggily.configuration

import com.loggily.loggily.repository.JsonStringToStructuredLogConverter
import com.loggily.loggily.repository.StructuredLogToJsonStringConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.H2Dialect

@Configuration(proxyBeanMethods = false)
class ReactiveDatabaseConfiguration {

    @Bean
    fun registerCustomizedR2dbcCustomConversions(): R2dbcCustomConversions {
        return R2dbcCustomConversions.of(H2Dialect.INSTANCE, listOf(
            StructuredLogToJsonStringConverter(),
            JsonStringToStructuredLogConverter()
        ))
    }
}