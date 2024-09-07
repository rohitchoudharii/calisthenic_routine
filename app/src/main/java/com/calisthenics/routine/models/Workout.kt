package com.calisthenics.routine.models

import com.calisthenics.routine.common.ObjectMapperUtil
import com.calisthenics.routine.config.converters.DayOfWeekConverter
import com.calisthenics.routine.config.converters.GenericConverter
import com.calisthenics.routine.config.converters.RoutineConverter
import com.calisthenics.routine.config.converters.UUIDListConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

@Entity data class Workout(
    @Id var dbId:Long = 0,
    @Convert(converter = UUIDListConverter::class, dbType = String::class)
    var routineIds: MutableList<UUID> = mutableListOf(),
    @Convert(converter = DayOfWeekConverter::class, dbType = String::class)
    var week: DayOfWeek = LocalDate.now().dayOfWeek
){
    override fun toString(): String {
        return ObjectMapperUtil.convertToString(this)
    }
}