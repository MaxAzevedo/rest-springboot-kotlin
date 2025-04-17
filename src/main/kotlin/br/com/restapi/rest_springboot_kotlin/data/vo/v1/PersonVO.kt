package br.com.restapi.rest_springboot_kotlin.data.vo.v1

import br.com.restapi.rest_springboot_kotlin.model.Person

data class PersonVO (

    var id: Long = 0,
    var firstName: String = "",

)
fun PersonVO.toEntity() = Person (this.id, this.firstName)


