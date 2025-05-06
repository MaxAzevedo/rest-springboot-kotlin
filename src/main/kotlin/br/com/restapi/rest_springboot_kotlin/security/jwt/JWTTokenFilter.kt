package br.com.restapi.rest_springboot_kotlin.security.jwt

import br.com.restapi.rest_springboot_kotlin.exception.InvalidJwtAuthenticationException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class JWTTokenFilter(
    @field: Autowired private val tokenProvider: JWTTokenProvider)
    : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain) {
        try {
            val token = tokenProvider.resolveToken(request as HttpServletRequest)
            if(!token.isNullOrBlank() && tokenProvider.validateToken(token)) {
                val auth = tokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
            chain.doFilter(request, response)
        } catch (e: Exception) {
            throw InvalidJwtAuthenticationException("Invalid token")
        }

    }
}