package br.com.restapi.rest_springboot_kotlin.service

import br.com.restapi.rest_springboot_kotlin.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UserService (@field:Autowired var repository: UserRepository) : UserDetailsService {

    private val logger = Logger.getLogger(UserService::class.java.name)

    override fun loadUserByUsername(userName: String?): UserDetails {
        logger.info("Find user by userName $userName")
        val user = repository.findByUserName(userName)
        return user ?: throw UsernameNotFoundException("Username $userName not found!")
    }


}