package br.com.restapi.rest_springboot_kotlin.model.repository

import br.com.restapi.rest_springboot_kotlin.model.Person
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<Person, Long?>