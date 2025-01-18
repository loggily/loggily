package com.loggily.loggily

import com.loggily.loggily.configuration.LoggilyPropertyConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(LoggilyPropertyConfig::class)
class LoggilyApplication

fun main(args: Array<String>) {
	runApplication<LoggilyApplication>(*args)
}
