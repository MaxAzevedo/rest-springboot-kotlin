package br.com.restapi.rest_springboot_kotlin.integrationtests.swagger

import br.com.restapi.rest_springboot_kotlin.integrationtests.ConfigTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest {

    @Test
    fun shouldDisplaySwaggerUIPage() {

        val content = given()
            .basePath("/swagger-ui/index.html")
            .port(ConfigTest.SERVER_PORT)
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract().body().asString()
        assertTrue(content.contains("Swagger UI"))
    }

}