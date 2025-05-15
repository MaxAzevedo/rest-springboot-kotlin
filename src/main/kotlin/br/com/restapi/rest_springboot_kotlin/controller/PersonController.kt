package br.com.restapi.rest_springboot_kotlin.controller

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PageVO
import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import br.com.restapi.rest_springboot_kotlin.service.PersonService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoint to handle People")
class PersonController {

    @Autowired
    private lateinit var service : PersonService

    @GetMapping
    @Operation(summary = "Find all people")
    fun getAll(@RequestBody page: PageVO) : ResponseEntity<PagedModel<EntityModel<PersonVO>>> {
        return ResponseEntity.ok(service.findAll(page))
    }

    @GetMapping(value = ["/{id}"])
    fun getPerson(@PathVariable(value = "id") id: Long) : PersonVO {
        return service.findById(id)
    }

    @PostMapping
    fun create(@RequestBody person: PersonVO) : PersonVO = service.create(person)

    @PutMapping
    fun update(@RequestBody person: PersonVO) : PersonVO = service.update(person)

    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable(value = "id") id: Long) : ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }

    @PatchMapping(value = ["/{id}"])
    fun disablePersonById(@PathVariable(value = "id") id: Long) : PersonVO {
        return service.disablePerson(id)
    }
}