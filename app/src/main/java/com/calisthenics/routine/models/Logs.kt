package com.calisthenics.routine.models

import com.calisthenics.routine.common.ObjectMapperUtil
import com.calisthenics.routine.config.converters.GenericConverter
import com.calisthenics.routine.config.converters.LogDetailsConverter
import com.calisthenics.routine.config.converters.UUIDConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.UUID

@Entity data class Logs(
    @Id var dbId: Long = 0,
    @Convert(converter = UUIDConverter::class, dbType = String::class)
    var id: UUID = UUID.randomUUID(),
    @Convert(converter = GenericConverter::class, dbType = String::class)
    var routine: Routine = Routine(),

    @Convert(converter = LogDetailsConverter::class, dbType = String::class)
    //@Convert(converter = GenericConverter::class, dbType = String::class)
    var logDetails: MutableList<LogDetail> = mutableListOf()
){
    override fun toString(): String {
        return ObjectMapperUtil.convertToString(this)
    }
}