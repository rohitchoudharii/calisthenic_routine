package com.calisthenics.routine.config.converters

import io.objectbox.converter.PropertyConverter
import java.util.UUID

class UUIDConverter : PropertyConverter<UUID, String> {

    override fun convertToDatabaseValue(entityProperty: UUID?): String? {
        return entityProperty?.toString()
    }

    override fun convertToEntityProperty(databaseValue: String?): UUID? {
        return if (databaseValue != null) UUID.fromString(databaseValue) else null
    }
}