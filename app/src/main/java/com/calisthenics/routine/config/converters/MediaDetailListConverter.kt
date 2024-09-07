package com.calisthenics.routine.config.converters

import com.calisthenics.routine.models.MediaDetail
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.objectbox.converter.PropertyConverter

class MediaDetailListConverter : PropertyConverter<MutableList<MediaDetail>, String> {

    private val objectMapper = ObjectMapper()


    override fun convertToDatabaseValue(entityProperty: MutableList<MediaDetail>?): String {
        return if (entityProperty == null) "" else objectMapper.writeValueAsString(entityProperty)
    }

    override fun convertToEntityProperty(databaseValue: String?): MutableList<MediaDetail> {
        if (databaseValue == null) return mutableListOf()
        return objectMapper.readValue(databaseValue, object : TypeReference<MutableList<MediaDetail>>() {})
    }
}