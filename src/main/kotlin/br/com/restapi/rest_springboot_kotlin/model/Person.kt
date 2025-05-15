package br.com.restapi.rest_springboot_kotlin.model

import br.com.restapi.rest_springboot_kotlin.data.vo.v1.PersonVO
import jakarta.persistence.*

@Entity
@Table(name = "person")
data class Person (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "first_name", nullable = false, length = 80)
    var firstName: String = "",

    @Column(nullable = false)
    var enabled: Boolean = true

)

fun Person.toVo() : PersonVO = PersonVO(this.id, this.firstName, this.enabled)

