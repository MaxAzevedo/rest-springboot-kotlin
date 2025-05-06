package br.com.restapi.rest_springboot_kotlin.repository

import br.com.restapi.rest_springboot_kotlin.model.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<Users?, Long?> {

    @Query("SELECT u FROM Users u WHERE u.userName =:userName")
    fun findByUserName(@Param("userName") userName: String?) : Users?
}