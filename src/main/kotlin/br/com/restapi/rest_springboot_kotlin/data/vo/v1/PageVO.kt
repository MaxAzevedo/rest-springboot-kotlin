package br.com.restapi.rest_springboot_kotlin.data.vo.v1

import org.springframework.data.domain.Sort

data class PageVO (
    val page: Int? = 0,
    val size: Int? = 10,
    val direction: Sort.Direction? = Sort.Direction.ASC,
    val totalPage: Int? = 1
)