package br.com.restapi.rest_springboot_kotlin.data.vo.v1

import java.util.Date

class TokenVO (
    val userName: String? = "",
    val authenticated: Boolean? = null,
    val createdDate: Date? = null,
    val expirationDate: Date? = null,
    val accessToken: String? = "",
    val refreshToken: String? = ""
)