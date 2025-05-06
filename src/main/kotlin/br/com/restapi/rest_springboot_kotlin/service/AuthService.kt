package br.com.restapi.rest_springboot_kotlin.service

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.AccountCredentialsVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.TokenVO
import br.com.restapi.rest_springboot_kotlin.repository.UserRepository
import br.com.restapi.rest_springboot_kotlin.security.jwt.JWTTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class AuthService {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var tokenProvider: JWTTokenProvider

    private val logger = Logger.getLogger(AuthService::class.java.name)

    fun singing(data: AccountCredentialsVO) : ResponseEntity<*> {
        logger.info("Trying log user ${data.userName}")
        return try {
            val username = data.userName
            val password = data.password
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            val user = repository.findByUserName(username)
            val tokenResponse: TokenVO = if (user != null) {
                tokenProvider.createAccessToken(username!!, user.roles)
            } else {
                throw UsernameNotFoundException("Username $username not found!")
            }
            ResponseEntity.ok(tokenResponse)
        } catch (e: AuthenticationException) {
            throw BadCredentialsException("Invalid username or password supplied!")
        }
    }
}