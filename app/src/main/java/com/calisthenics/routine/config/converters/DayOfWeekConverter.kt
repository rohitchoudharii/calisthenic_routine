package com.calisthenics.routine.config.converters

import io.objectbox.converter.PropertyConverter
import java.time.DayOfWeek

class DayOfWeekConverter : PropertyConverter<DayOfWeek, String> {

    override fun convertToDatabaseValue(entityProperty:DayOfWeek ?):String ? {
        return entityProperty ?.name
    }

    override fun convertToEntityProperty(databaseValue:String ?):DayOfWeek ? {
        return databaseValue ?.let {
            DayOfWeek.valueOf(it)
        }
    }
}