package com.loggily.loggily.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : CoroutineCrudRepository<Log, Long>