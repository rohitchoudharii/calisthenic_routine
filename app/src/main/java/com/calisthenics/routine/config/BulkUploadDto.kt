package com.calisthenics.routine.config

import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.models.Routine
import com.calisthenics.routine.models.Workout

data class BulkUploadDto(
    val reset: Boolean = false,
    val exercises: List<Exercise> = mutableListOf(),
    val routines: List<Routine> = mutableListOf(),
    val workouts: List<Workout> = mutableListOf()
)