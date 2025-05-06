package br.com.restapi.rest_springboot_kotlin.controller

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.AccountCredentialsVO
import br.com.restapi.rest_springboot_kotlin.service.AuthService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Authentication", description = "Endpoint to authenticate")
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var authService: AuthService

    @PostMapping(value = ["/signin"])
    fun singing(@RequestBody data: AccountCredentialsVO?): ResponseEntity<*> {
        return if (data!!.userName.isNullOrBlank() || data.password.isNullOrBlank())
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request")
        else authService.singing(data)
    }
}