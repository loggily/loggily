package com.loggily.loggily

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<LoggilyApplication>().with(TestcontainersConfiguration::class).run(*args)
}
