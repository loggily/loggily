package com.loggily.loggily

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoggilyApplication

fun main(args: Array<String>) {
	runApplication<LoggilyApplication>(*args)
}
