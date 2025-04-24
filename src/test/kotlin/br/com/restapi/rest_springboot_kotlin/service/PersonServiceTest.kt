package br.com.restapi.rest_springboot_kotlin.service

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import br.com.restapi.rest_springboot_kotlin.model.Person
import br.com.restapi.rest_springboot_kotlin.model.repository.PersonRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class PersonServiceTest {

    @InjectMocks
    private lateinit var service : PersonService

    @Mock
    private lateinit var repository: PersonRepository

    @BeforeEach
    fun setUpMock() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findByIdTest(){
        val person = Person(1, "Max")
        `when`(repository.findById(person.id)).thenReturn(Optional.of(person))
        val result = service.findById(person.id)
        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/person/v1/1"))
        assertEquals(result.firstName, "Max")
    }

    @Test
    fun createTest(){
        val person = Person(firstName =  "Max")
        `when`(repository.save(person)).thenReturn(person.copy(id = 1, firstName = "Max"))
        val result = service.create(PersonVO(firstName = "Max"))
        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/person/v1/1"))
        assertEquals(result.firstName, "Max")
    }

    @Test
    fun updateTest(){
        val person = Person(id = 1, firstName =  "Max")
        `when`(repository.findById(person.id)).thenReturn(Optional.of(person))
        `when`(repository.save(person)).thenReturn(person.copy(id = 1, firstName = "Max Updated"))

        val result = service.update(PersonVO(key = 1, firstName = "Max Updated"))

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        assertTrue(result.links.toString().contains("/person/v1/1"))
        assertEquals(result.firstName, "Max Updated")
    }
}