package com.calisthenics.routine.models

import com.calisthenics.routine.common.ObjectMapperUtil
import com.calisthenics.routine.config.converters.GenericConverter
import com.calisthenics.routine.config.converters.RoutineDetailsConverter
import com.calisthenics.routine.config.converters.UUIDConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.UUID

@Entity data class Routine(
    @Id var dbId: Long = 0,
    @Convert(converter = UUIDConverter::class, dbType = String::class)
    var id: UUID = UUID.randomUUID(),
    var name: String = "",
    var description: String = "",
    var tags: MutableList<String> = mutableListOf(),

    @Convert(converter = RoutineDetailsConverter::class, dbType = String::class)
    var routineDetails: MutableList<RoutineDetail> = mutableListOf()
){
    override fun toString(): String {
        return ObjectMapperUtil.convertToString(this)
    }
}