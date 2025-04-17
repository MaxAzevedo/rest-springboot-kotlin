package br.com.restapi.rest_springboot_kotlin.exception

import java.sql.Timestamp
import java.util.*

class ExceptionResponse (
    val timestamp: Date,
    val message: String?,
    val detail: String
)