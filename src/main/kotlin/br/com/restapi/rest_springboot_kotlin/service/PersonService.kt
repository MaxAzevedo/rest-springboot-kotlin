package br.com.restapi.rest_springboot_kotlin.service

import br.com.restapi.rest_springboot_kotlin.controller.PersonController
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.toEntity
import br.com.restapi.rest_springboot_kotlin.exception.ResourceNotFoundException
import br.com.restapi.rest_springboot_kotlin.model.Person
import br.com.restapi.rest_springboot_kotlin.model.repository.PersonRepository
import br.com.restapi.rest_springboot_kotlin.model.toVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository
    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll() : List<PersonVO> {
        logger.info("Find all people")
        val personList = repository.findAll().map { it.toVo() }
        return personList.map { it.add(linkTo(PersonController::class.java).slash(it.key).withSelfRel()) }
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