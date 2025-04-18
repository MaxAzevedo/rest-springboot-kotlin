package br.com.restapi.rest_springboot_kotlin.controller

import br.com.restapi.rest_springboot_kotlin.model.Greeting
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class GreetingController {

    val counter : AtomicLong = AtomicLong()

    @RequestMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "world") name: String): Greeting {
       return Greeting(counter.incrementAndGet(), "Hello, $name!")
    }
}