package com.calisthenics.routine.models

import com.calisthenics.routine.common.ObjectMapperUtil
import java.util.UUID

data class RoutineDetail(
    var exerciseId: UUID = UUID.randomUUID(),
    var exerciseMetric: ExerciseMetric = ExerciseMetric(),
    var order: Int = 0
){
    override fun toString(): String {
        return ObjectMapperUtil.convertToString(this)
    }
}