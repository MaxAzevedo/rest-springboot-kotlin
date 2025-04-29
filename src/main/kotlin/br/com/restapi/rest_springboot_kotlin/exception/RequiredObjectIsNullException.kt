package br.com.restapi.rest_springboot_kotlin.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequiredObjectIsNullException : RuntimeException{
    constructor(): super("It is not allowed")
    constructor(exception: String?): super(exception)
}