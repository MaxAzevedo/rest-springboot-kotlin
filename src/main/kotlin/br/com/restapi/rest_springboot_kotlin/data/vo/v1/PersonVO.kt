package br.com.restapi.rest_springboot_kotlin.data.vo.v1

import br.com.restapi.rest_springboot_kotlin.model.Person
import org.springframework.hateoas.RepresentationModel

data class PersonVO (

    var key: Long = 0,
    var firstName: String = "",

    ) : RepresentationModel<PersonVO>()
fun PersonVO.toEntity() = Person (this.key, this.firstName)


