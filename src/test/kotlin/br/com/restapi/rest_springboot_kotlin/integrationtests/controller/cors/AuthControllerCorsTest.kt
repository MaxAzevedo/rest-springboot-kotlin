package br.com.restapi.rest_springboot_kotlin.integrationtests.controller.cors

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.AccountCredentialsVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.TokenVO
import br.com.restapi.rest_springboot_kotlin.integrationtests.TestConfig
import br.com.restapi.rest_springboot_kotlin.integrationtests.testcontainers.AbstractIntegrationTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerCorsTest : AbstractIntegrationTest(){

    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTests() {
        tokenVO = TokenVO()
    }

    @Test
    @Order(1)
    fun singingTest() {
        val user = AccountCredentialsVO(
            userName = "max",
            password = "admin123"
        )

        tokenVO = given()
            .basePath("/auth/singing")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract().body().`as`(TokenVO::class.java)

        assertNotNull(tokenVO)
        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
        assertTrue(tokenVO.authenticated!!)
    }

    @Test
    @Order(2)
    fun refreshTest() {
        tokenVO = given()
            .basePath("/auth/refresh")
            .port(TestConfig.SERVER_PORT)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .pathParams("userName", tokenVO.userName)
            .header(TestConfig.HEADER_PARAM_AUTHORIZATION, "Bearer ${tokenVO.refreshToken}")
            .`when`()
            .put("{userName}")
            .then()
            .statusCode(200)
            .extract().body().`as`(TokenVO::class.java)

        assertNotNull(tokenVO)
        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
        assertTrue(tokenVO.authenticated!!)
    }
}