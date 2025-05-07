package br.com.restapi.rest_springboot_kotlin.security.jwt

import br.com.restapi.rest_springboot_kotlin.exception.handler.JwtTokenFilterExceptionHandler
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTTokenFilter(
    @field: Autowired private val tokenProvider: JWTTokenProvider,
    @field: Autowired private val jwtTokenFilterExceptionHandler: JwtTokenFilterExceptionHandler
)
    : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = tokenProvider.resolveToken(request)
            if (!token.isNullOrBlank() && tokenProvider.validateToken(token)) {
                val auth = tokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
            filterChain.doFilter(request, response)
        } catch (ex: Exception) {
            jwtTokenFilterExceptionHandler.resolveException(request, response, null, ex)
        }
    }
}