package com.calisthenics.routine.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.models.Exercise_
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.objectbox.Box
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import java.io.InputStream
import java.util.UUID

class ExerciseViewModel(private val exerciseBox: Box<Exercise>) : ViewModel() {

    fun saveExercise(exercise: Exercise) {
        Log.d("ExerciseViewModel", "Saving in references $exercise")
        exerciseBox.put(exercise)
    }

    fun getExercise(id: UUID): Exercise {
        val exercises: List<Exercise> = exerciseBox.query {
            equal(
                Exercise_.id,
                id.toString(),
                QueryBuilder.StringOrder.CASE_SENSITIVE
            )
        }.find()
        if (exercises.isEmpty()) {
            return Exercise()
        }
        return exercises[0]
    }

    fun getAllExercises(): MutableList<Exercise> {
        return exerciseBox.all
    }

    fun delete(exercise: Exercise) {
        exerciseBox.remove(exercise.dbId)
    }


}