package com.loggily.loggily.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

val objectMapper = jacksonObjectMapper()

@WritingConverter
class StructuredLogToJsonStringConverter : Converter<StructuredLog, String> {
    override fun convert(source: StructuredLog): String = objectMapper.writeValueAsString(source)
}

@ReadingConverter
class JsonStringToStructuredLogConverter : Converter<String, StructuredLog> {
    override fun convert(source: String): StructuredLog = objectMapper.readValue(source, StructuredLog::class.java)
}