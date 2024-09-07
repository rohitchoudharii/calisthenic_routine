package com.calisthenics.routine.models

import com.calisthenics.routine.common.ObjectMapperUtil

data class ExerciseMetric(
    var sets: Int = 0,
    var repetations: Int = 0,
    var holds: Int = 0
){
    override fun toString(): String {
        return ObjectMapperUtil.convertToString(this)
    }
}
