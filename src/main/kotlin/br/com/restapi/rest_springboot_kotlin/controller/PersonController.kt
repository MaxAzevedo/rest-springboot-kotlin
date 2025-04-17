package br.com.restapi.rest_springboot_kotlin.controller

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import br.com.restapi.rest_springboot_kotlin.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/person/v1/")
class PersonController {

    @Autowired
    private lateinit var service : PersonService

    @GetMapping
    fun getAll() : List<PersonVO> = service.findAll()

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
}