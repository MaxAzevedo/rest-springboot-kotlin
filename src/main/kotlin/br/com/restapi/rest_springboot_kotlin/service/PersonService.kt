package br.com.restapi.rest_springboot_kotlin.service

import br.com.restapi.rest_springboot_kotlin.controller.PersonController
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PageVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.toEntity
import br.com.restapi.rest_springboot_kotlin.exception.ResourceNotFoundException
import br.com.restapi.rest_springboot_kotlin.model.Person
import br.com.restapi.rest_springboot_kotlin.repository.PersonRepository
import br.com.restapi.rest_springboot_kotlin.model.toVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var assembler: PagedResourcesAssembler<PersonVO>

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(page : PageVO) : PagedModel<EntityModel<PersonVO>> {
        logger.info("Find all people")
        val pageable: Pageable =
            PageRequest.of(
                page.page!!,
                page.size!!,
                Sort.by(page.direction!!, "firstName")
            )
        val personList = repository.findAll(pageable).map { it.toVo() }
        return assembler.toModel(
            personList.map {
                it.add(linkTo(PersonController::class.java).slash(it.key).withSelfRel())
            }
        )
    }

    fun findById(id: Long) : PersonVO {
        logger.info("Find a person")
        val personVO = findPersonById(id).toVo()
        personVO.add(linkTo(PersonController::class.java).slash(personVO.key).withSelfRel())
        return personVO
    }

    fun create(person: PersonVO) : PersonVO {
        logger.info("Create a person with name ${person.firstName}")
        val personVO = repository.save(person.toEntity()).toVo()
        personVO.add(linkTo(PersonController::class.java).slash(personVO.key).withSelfRel())
        return personVO
    }

    @Transactional
    fun disablePerson(id: Long) : PersonVO{
        logger.info("Disable a person")
        repository.disablePerson(id)
        return findPersonById(id).toVo()
    }

    fun update(person: PersonVO) : PersonVO {
        logger.info("Updating a person with id: $person.id")
        val entity = findPersonById(person.key)
        entity.firstName = person.firstName
        val personVO = repository.save(entity).toVo()
        personVO.add(linkTo(PersonController::class.java).slash(personVO.key).withSelfRel())
        return personVO
    }

    fun delete(id: Long) {
        logger.info("Delete a person with id: $id")
        repository.delete(findPersonById(id))
    }

    private fun findPersonById(id: Long): Person =
        repository.findById(id)
            .orElseThrow({ ResourceNotFoundException("No records found for id: $id") })
}