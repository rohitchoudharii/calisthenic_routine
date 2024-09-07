package com.calisthenics.routine.ViewModels

import androidx.lifecycle.ViewModel
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Workout
import com.calisthenics.routine.models.Workout_
import io.objectbox.Box
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.stream.Collectors

class WorkoutViewModel(private val workoutBox: Box<Workout>) : ViewModel() {
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)

    fun getAllWorkouts(): MutableList<Workout> {
        var workouts = workoutBox.all
        if (workouts.size != 7) {
            for (i in 0 until 7) {
                workoutBox.put(Workout(week = DayOfWeek.of(i + 1)))
            }
        }
        return workoutBox.all
    }

    fun saveWorkout(workout: Workout) {
        workout.routineIds =
            workout.routineIds.stream().filter { routineViewModel.getRoutine(it).name.isNotEmpty() }
                .collect(
                    Collectors.toList()
                )
        workoutBox.put(workout)
    }

    fun getWorkoutByWeek(dayOfWeek: DayOfWeek): Workout {
        var workouts = workoutBox.query {
            equal(
                Workout_.week,
                dayOfWeek.name,
                QueryBuilder.StringOrder.CASE_SENSITIVE
            )
        }.find()
        if (workouts.isEmpty()) {
            return Workout(week = dayOfWeek)
        }

        return workouts[0]
    }
}