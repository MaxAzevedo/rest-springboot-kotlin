package br.com.restapi.rest_springboot_kotlin.exception.handler

import br.com.restapi.rest_springboot_kotlin.exception.ExceptionResponse
import br.com.restapi.rest_springboot_kotlin.exception.InvalidJwtAuthenticationException
import br.com.restapi.rest_springboot_kotlin.exception.RequiredObjectIsNullException
import br.com.restapi.rest_springboot_kotlin.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
@RestController
class CustomizeResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RuntimeException::class)
    fun handleAllException(ex: Exception, request: WebRequest) :
            ResponseEntity<ExceptionResponse>{
        val exceptionResponse = ExceptionResponse(
            timestamp = Date(),
            message = ex.message,
            detail = request.getDescription(false)
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: Exception, request: WebRequest) :
            ResponseEntity<ExceptionResponse>{
        val exceptionResponse = ExceptionResponse(
            timestamp = Date(),
            message = ex.message,
            detail = request.getDescription(false)
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(RequiredObjectIsNullException::class)
    fun handleBadRequestException(ex: Exception, request: WebRequest) :
            ResponseEntity<ExceptionResponse>{
        val exceptionResponse = ExceptionResponse(
            timestamp = Date(),
            message = ex.message,
            detail = request.getDescription(false)
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidJwtAuthenticationException::class)
    fun handleInvalidJwtAuthenticationException(ex: Exception, request: WebRequest) :
            ResponseEntity<ExceptionResponse>{
        val exceptionResponse = ExceptionResponse(
            timestamp = Date(),
            message = ex.message,
            detail = request.getDescription(false)
        )
        return ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.FORBIDDEN)
    }
}