package br.com.restapi.rest_springboot_kotlin.service

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.toEntity
import br.com.restapi.rest_springboot_kotlin.exception.ResourceNotFoundException
import br.com.restapi.rest_springboot_kotlin.model.Person
import br.com.restapi.rest_springboot_kotlin.model.repository.PersonRepository
import br.com.restapi.rest_springboot_kotlin.model.toVo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var repository: PersonRepository
    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll() : List<PersonVO> {
        logger.info("Find all people")
        return repository.findAll().map { it.toVo() }
    }

    fun findById(id: Long) : PersonVO {
        logger.info("Find a person")
        return findPersonById(id).toVo()
    }

    fun create(person: PersonVO) : PersonVO {
        logger.info("Create a person with name ${person.firstName}")
        return repository.save(person.toEntity()).toVo()
    }

    fun update(person: PersonVO) : PersonVO {
        logger.info("Updating a person with id: $person.id")
        val entity = repository.findById(person.id)
            .orElseThrow({ ResourceNotFoundException("No records found for id: $person.id") })
        entity.firstName = person.firstName
        return repository.save(entity).toVo()
    }

    fun delete(id: Long) {
        logger.info("Delete a person with id: $id")
        repository.delete(findPersonById(id))
    }

    private fun findPersonById(id: Long): Person =
        repository.findById(id)
            .orElseThrow({ ResourceNotFoundException("No records found for id: $id") })
}