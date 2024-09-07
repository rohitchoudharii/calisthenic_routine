package com.calisthenics.routine.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.ViewModels.LogsViewModel
import com.calisthenics.routine.ViewModels.RoutineViewModel
import com.calisthenics.routine.ViewModels.WorkoutViewModel
import com.calisthenics.routine.application.ObjectBoxApplication
import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.models.Logs
import com.calisthenics.routine.models.Routine
import com.calisthenics.routine.models.Workout

class ViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val boxStore = ObjectBoxApplication.getBoxStore()
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            return ExerciseViewModel(boxStore.boxFor(Exercise::class.java)) as T
        }
        if (modelClass.isAssignableFrom(LogsViewModel::class.java)) {
            return LogsViewModel(boxStore.boxFor(Logs::class.java)) as T
        }
        if (modelClass.isAssignableFrom(RoutineViewModel::class.java)) {
            return RoutineViewModel(boxStore.boxFor(Routine::class.java)) as T
        }
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(boxStore.boxFor(Workout::class.java)) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
