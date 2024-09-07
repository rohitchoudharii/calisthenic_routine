package com.calisthenics.routine.config.converters

import com.calisthenics.routine.models.RoutineDetail
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.objectbox.converter.PropertyConverter
import java.util.UUID


class UUIDListConverter : PropertyConverter<MutableList<UUID>, String> {

    private val objectMapper = ObjectMapper()


    override fun convertToDatabaseValue(entityProperty: MutableList<UUID>?): String {
        return if (entityProperty == null) "" else objectMapper.writeValueAsString(entityProperty)
    }

    override fun convertToEntityProperty(databaseValue: String?): MutableList<UUID> {
        if (databaseValue == null) return mutableListOf()
        return objectMapper.readValue(databaseValue, object : TypeReference<MutableList<UUID>>() {})
    }
}