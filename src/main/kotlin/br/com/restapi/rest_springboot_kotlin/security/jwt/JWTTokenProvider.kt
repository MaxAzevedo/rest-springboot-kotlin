package br.com.restapi.rest_springboot_kotlin.security.jwt

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.TokenVO
import br.com.restapi.rest_springboot_kotlin.exception.InvalidJwtAuthenticationException
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

@Service
class JWTTokenProvider {
    @Value("\${security.jwt.token.secret-key:secret}")
    private var secretKey = "secret"

    @Value("\${security.jwt.token.expire-length:360000}")
    private var validityTime: Long = 3_600_000

    @Autowired
    private lateinit var userDetailService: UserDetailsService

    private lateinit var algorithm: Algorithm

    private val BEARER = "Bearer "
    private val ROLES = "roles"

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
        algorithm = Algorithm.HMAC256(secretKey.toByteArray())
    }

    fun createAccessToken(userName: String, roles: List<String?> ) : TokenVO {
        val now = Date()
        val validity = Date(now.time + validityTime)
        val accessToken = getAccessToken(userName, roles, now, validity)
        val refreshToken = getRefreshToken(userName, roles, now)
        return TokenVO(
            userName = userName,
            authenticated = true,
            createdDate = now,
            expirationDate = validity,
            accessToken = accessToken,
            refreshToken = refreshToken)
    }

    fun createRefreshToken(refreshToken: String) : TokenVO {
        var token = ""
        if(refreshToken.contains(BEARER)) token = refreshToken.substring(BEARER.length)
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        val decodedJWT: DecodedJWT = verifier.verify(token)
        val userName = decodedJWT.subject
        val roles = decodedJWT.getClaim("roles").asList(String::class.java)
        return createAccessToken(userName, roles)
    }

    private fun getAccessToken(userName: String, roles: List<String?>, now: Date, validity: Date): String {
        val issuerUrl: String = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        return JWT
            .create()
            .withClaim(ROLES, roles)
            .withIssuedAt(now)
            .withExpiresAt(validity)
            .withSubject(userName)
            .withIssuer(issuerUrl)
            .sign(algorithm)
            .trim()
    }

    private fun getRefreshToken(userName: String, roles: List<String?>, now: Date): String {
        val validityRefreshToken = Date(now.time + validityTime)
        return JWT
            .create()
            .withClaim(ROLES, roles)
            .withExpiresAt(validityRefreshToken)
            .withSubject(userName)
            .sign(algorithm)
            .trim()
    }

    fun getAuthentication(token: String) : Authentication {
        val decodeJWT: DecodedJWT = decodedToken(token)
        val userDetails : UserDetails = userDetailService.loadUserByUsername(decodeJWT.subject)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun decodedToken(token: String): DecodedJWT {
        val algorithm = Algorithm.HMAC256(secretKey.toByteArray())
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        return verifier.verify(token)
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if(!bearerToken.isNullOrBlank() && bearerToken.startsWith(BEARER)) {
            bearerToken.substring(BEARER.length)
        } else null
    }

    fun validateToken(token: String): Boolean {
        val decodedJWT = decodedToken(token)
        try {
            return !decodedJWT.expiresAt.before(Date())
        } catch (e: Exception) {
            throw InvalidJwtAuthenticationException("Expired or invalid JWT token!")
        }
    }
}