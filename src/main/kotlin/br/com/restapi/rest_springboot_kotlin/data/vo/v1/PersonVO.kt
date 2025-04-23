package br.com.restapi.rest_springboot_kotlin.data.vo.v1

import br.com.restapi.rest_springboot_kotlin.model.Person
import jakarta.persistence.Column
import org.springframework.hateoas.RepresentationModel

data class PersonVO (

    @Column(name = "id")
    var key: Long = 0,
    var firstName: String = "",

    ) : RepresentationModel<PersonVO>()
fun PersonVO.toEntity() = Person (this.key, this.firstName)


