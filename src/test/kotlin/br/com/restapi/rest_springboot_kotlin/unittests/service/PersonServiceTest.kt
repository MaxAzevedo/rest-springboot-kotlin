package br.com.restapi.rest_springboot_kotlin.unittests.service

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PageVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import br.com.restapi.rest_springboot_kotlin.model.Person
import br.com.restapi.rest_springboot_kotlin.model.toVo
import br.com.restapi.rest_springboot_kotlin.repository.PersonRepository
import br.com.restapi.rest_springboot_kotlin.service.PersonService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel.PageMetadata
import org.springframework.hateoas.PagedModel.of
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

    @Mock
    private lateinit var assembler: PagedResourcesAssembler<PersonVO>

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

    @Test
    fun findAllTest(){
        val person = Person(id = 1, firstName =  "Max")
        val pageable = PageImpl(listOf(person))
        val pageVO = PageVO()
        `when`(repository.findAll(
            PageRequest.of(
                pageVO.page!!,
                pageVO.size!!,
                Sort.by(pageVO.direction!!, "firstName")
        ))).thenReturn(pageable)

        `when`(assembler.toModel(any())).thenReturn(
            of(
                listOf(EntityModel.of(person.toVo())),
                PageMetadata(0, 1, 10)
            )
        )

        val result = service.findAll(pageVO)
        assertNotNull(result.content)
        assertNotNull(result.content.first().content)
        assertEquals(result.content.first().content!!.key, person.id)
        assertEquals(result.content.first().content!!.firstName, person.firstName)
    }
}