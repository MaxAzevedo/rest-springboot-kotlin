package br.com.restapi.rest_springboot_kotlin.integrationtests.controller.cors

import br.com.restapi.rest_springboot_kotlin.integrationtests.TestConfig
import br.com.restapi.rest_springboot_kotlin.integrationtests.testcontainers.AbstractIntegrationTest
import br.com.restapi.rest_springboot_kotlin.integrationtests.vo.PersonVO
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerCorsTest : AbstractIntegrationTest(){

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO

    @BeforeAll
    fun setupTests() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO(1, "Teste")
    }

    @Test
    @Order(1)
    fun createTest() {
        specification = RequestSpecBuilder()
            .addHeader(
                TestConfig.HEADER_PARAM_ORIGIN,
                TestConfig.ORIGIN_LOCALHOST)
            .setBasePath("/api/person/v1/")
            .setPort(TestConfig.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        val content = given()
            .spec(specification)
            .contentType(TestConfig.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract().body().asString()

        val personCreated = objectMapper.readValue(
            content,
            PersonVO::class.java
        )
        assertNotNull(personCreated)
        assertNotNull(personCreated.id)
        assertNotNull(personCreated.firstName)
        assertEquals(personCreated.id, person.id)
        assertEquals(personCreated.firstName, person.firstName)
    }
}