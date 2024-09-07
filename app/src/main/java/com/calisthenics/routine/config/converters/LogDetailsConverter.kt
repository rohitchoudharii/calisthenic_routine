package com.calisthenics.routine.config.converters

import com.calisthenics.routine.models.LogDetail
import com.calisthenics.routine.models.RoutineDetail
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.objectbox.converter.PropertyConverter

class LogDetailsConverter : PropertyConverter<MutableList<LogDetail>, String> {

    private val objectMapper = ObjectMapper()


    override fun convertToDatabaseValue(entityProperty: MutableList<LogDetail>?): String {
        return if (entityProperty == null) "" else objectMapper.writeValueAsString(entityProperty)
    }

    override fun convertToEntityProperty(databaseValue: String?): MutableList<LogDetail> {
        if (databaseValue == null) return mutableListOf()
        return objectMapper.readValue(databaseValue, object : TypeReference<MutableList<LogDetail>>() {})
    }
}