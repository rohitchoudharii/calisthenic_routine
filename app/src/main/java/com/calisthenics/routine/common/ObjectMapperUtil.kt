package com.calisthenics.routine.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class ObjectMapperUtil{

    companion object {
        var objectMapper = jacksonObjectMapper()
        fun convertToString(obj: Any): String{
            return objectMapper.writeValueAsString(obj)
        }
    }

}




