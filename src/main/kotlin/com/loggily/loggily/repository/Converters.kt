package com.loggily.loggily.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.r2dbc.postgresql.codec.Json
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

val objectMapper = jacksonObjectMapper()

@WritingConverter
class StructuredLogToJsonConverter : Converter<StructuredLog, Json> {
    override fun convert(source: StructuredLog): Json = Json.of(objectMapper.writeValueAsString(source))
}

@ReadingConverter
class JsonToStructuredLogConverter : Converter<Json, StructuredLog> {
    override fun convert(source: Json): StructuredLog =
        objectMapper.readValue(source.asString(), StructuredLog::class.java)
}