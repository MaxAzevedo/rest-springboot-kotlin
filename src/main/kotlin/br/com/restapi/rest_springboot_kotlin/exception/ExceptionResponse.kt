package br.com.restapi.rest_springboot_kotlin.exception

import java.sql.Timestamp
import java.util.*
import kotlin.reflect.full.memberProperties

class ExceptionResponse (
    val timestamp: Date,
    val message: String?,
    val detail: String
)

fun ExceptionResponse.asMap() = this::class.memberProperties.associate { it.name to it.getter.call(this)}