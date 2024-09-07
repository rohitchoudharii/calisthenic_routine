package com.calisthenics.routine.config.converters

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.objectbox.converter.PropertyConverter

class GenericConverter<T> : PropertyConverter<T, String> {

    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseValue(entityProperty: T?): String? {
        return if (entityProperty == null) null else objectMapper.writeValueAsString(entityProperty)
    }

    override fun convertToEntityProperty(databaseValue: String?): T? {
        if (databaseValue == null) return null
        val typeReference = object : TypeReference<T>() {}
        return objectMapper.readValue(databaseValue, typeReference)
    }
}