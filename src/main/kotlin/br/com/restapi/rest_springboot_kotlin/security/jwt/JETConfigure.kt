package br.com.restapi.rest_springboot_kotlin.security.jwt

import br.com.restapi.rest_springboot_kotlin.exception.handler.JwtTokenFilterExceptionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JETConfigure(@field: Autowired private val tokenProvider: JWTTokenProvider,
                   @field: Autowired private val jwtTokenFilterExceptionHandler: JwtTokenFilterExceptionHandler
)
    : SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>()  {

    override fun configure(builder: HttpSecurity) {
        val customFilter = JWTTokenFilter(tokenProvider, jwtTokenFilterExceptionHandler)
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}