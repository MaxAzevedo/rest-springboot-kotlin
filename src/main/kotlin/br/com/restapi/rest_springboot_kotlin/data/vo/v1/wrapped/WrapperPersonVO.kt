package br.com.restapi.rest_springboot_kotlin.data.vo.v1.wrapped

import com.fasterxml.jackson.annotation.JsonProperty

class WrapperPersonVO {

    @JsonProperty("_embedded")
    var embbeded: PersonEmbeddedVO? = null
}