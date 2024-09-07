package com.calisthenics.routine.models

import com.calisthenics.routine.common.ObjectMapperUtil


class LogDetail(
    var routineDetail: RoutineDetail = RoutineDetail(),
    var completedExerciseMetrics: MutableList<ExerciseMetric> = mutableListOf()
){
    override fun toString(): String {
        return ObjectMapperUtil.convertToString(this)
    }
}