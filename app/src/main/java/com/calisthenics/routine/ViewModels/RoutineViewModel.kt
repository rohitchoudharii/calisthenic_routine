package com.calisthenics.routine.ViewModels

import androidx.lifecycle.ViewModel
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Routine
import com.calisthenics.routine.models.RoutineDetail
import com.calisthenics.routine.models.Routine_
import io.objectbox.Box
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import java.util.UUID
import java.util.stream.Collectors

class RoutineViewModel(private val routineBox: Box<Routine>) : ViewModel() {
    private var exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)

    fun getAllRoutines(): List<Routine> {
        return routineBox.all
    }

    fun saveRoutine(routine: Routine) {
        routine.routineDetails = routine.routineDetails.stream().filter {
            exerciseViewModel.getExercise(it.exerciseId).name.isNotEmpty()
        }.collect(Collectors.toList())

        routineBox.put(routine)
    }

    fun getRoutine(id: UUID): Routine {
        val routines = routineBox.query {
            equal(Routine_.id, id.toString(), QueryBuilder.StringOrder.CASE_SENSITIVE)
        }.find()
        if (routines.isEmpty()) {
            return Routine()
        }
        return routines[0]
    }

    fun deleteRoutine(routine: Routine) {
        routineBox.remove(routine.dbId)
    }
}