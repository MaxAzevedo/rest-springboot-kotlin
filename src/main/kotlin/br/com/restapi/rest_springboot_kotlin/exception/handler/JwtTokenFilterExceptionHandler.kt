package br.com.restapi.rest_springboot_kotlin.exception.handler

import br.com.restapi.rest_springboot_kotlin.exception.ExceptionResponse
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Component
class JwtTokenFilterExceptionHandler(private val objectMapper: ObjectMapper) : HandlerExceptionResolver {

    override fun resolveException(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any?,
        ex: Exception
    ): ModelAndView? {
        when (ex) {
            is SignatureVerificationException -> {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                val errorResponse = ExceptionResponse(
                    timestamp = Date(),
                    message = "Invalid token",
                    detail = request.requestURI)
                response.writer.write(objectMapper.writeValueAsString(errorResponse))
                return ModelAndView()
            }

            is TokenExpiredException -> {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.contentType = "application/json"
                val errorResponse = ExceptionResponse(
                    timestamp = Date(),
                    message = "Expired token",
                    detail = request.requestURI)
                response.writer.write(objectMapper.writeValueAsString(errorResponse))
                return ModelAndView()
            }
            // Handle other JWT related exceptions or any other exception
            else -> return ModelAndView() // Let Spring handle other exceptions
        }
    }
}