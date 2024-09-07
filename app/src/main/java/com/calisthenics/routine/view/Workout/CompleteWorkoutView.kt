package com.calisthenics.routine.view.Workout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.ExerciseMetric
import com.calisthenics.routine.models.Logs
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent

val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)

@Composable
fun CompleteWorkoutViewController(log: Logs) {
    val completeWorkoutViewController = rememberNavController()

    BackHandler {
        NavigationUtil.navigate(completeWorkoutViewController, Screens.WORKOUT_HOME.screen)
    }

    NavHost(
        navController = completeWorkoutViewController,
        startDestination = Screens.FINISH_WORKOUT.screen
    ) {
        composable(Screens.FINISH_WORKOUT.screen) {
            ScreenHeader(
                title = "Workout Home",
                onBackButtonClick = { NavigationUtil.navigate(completeWorkoutViewController, Screens.WORKOUT_HOME.screen) }) {
                CompleteWorkoutView(log, completeWorkoutViewController)
            }
        }
        composable(Screens.WORKOUT_HOME.screen) {
            WorkoutHomeViewController()
        }
    }
}

@Composable
fun CompleteWorkoutView(log: Logs, completeWorkoutViewController: NavHostController) {
    Box(
        Modifier
            .padding(5.dp)
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        Column {
            Box(
                Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()
            ) {

                LazyColumn {
                    items(log.logDetails) { logDetail ->
                        val routineDetail = logDetail.routineDetail
                        val exercise = exerciseViewModel.getExercise(routineDetail.exerciseId)
                        val targetMatrix = logDetail.routineDetail.exerciseMetric
                        logDetail.completedExerciseMetrics.forEachIndexed { i, cm ->
                            cm.sets = i + 1
                        }
                        val completedMatrixes = logDetail.completedExerciseMetrics
                        Column {
                            TextDetailsComponent(
                                text = exercise.name,
                                title = "Name",
                                isColumn = false
                            )
                            RenderMatrix(
                                targetMatrix,
                                "Target",
                                boxModifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                                    .background(Color.Blue.copy(alpha = 0.5F))
                            )

                            completedMatrixes.forEach { completedMatrix ->
                                RenderMatrix(
                                    completedMatrix,
                                    "Completed",
                                    boxModifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .background(Color.Green.copy(alpha = 0.5F))
                                )
                            }
                        }
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Button(onClick = { saveWorkout(log) }) {
                        Text("Save workout")
                    }
                    Button(onClick = { NavigationUtil.navigate(completeWorkoutViewController, Screens.WORKOUT_HOME.screen)  }) {
                        Text("Go to home page")
                    }
                }
            }
        }
    }
}

@Composable
private fun RenderMatrix(
    exerciseMetric: ExerciseMetric,
    initial: String,
    boxModifier: Modifier = Modifier
) {
    Box(modifier = boxModifier) {
        Column {
            if (exerciseMetric.sets != 0) {
                TextDetailsComponent(
                    text = exerciseMetric.sets.toString(),
                    title = "$initial Sets",
                    fontMultiplier = 1F,
                    isColumn = false
                )
            }
            if (exerciseMetric.repetations != 0) {
                TextDetailsComponent(
                    text = exerciseMetric.repetations.toString(),
                    title = "$initial Reps",
                    fontMultiplier = 1F,
                    isColumn = false
                )
            }
            if (exerciseMetric.holds != 0) {
                TextDetailsComponent(
                    text = exerciseMetric.holds.toString(),
                    title = "$initial Reps",
                    fontMultiplier = 1F,
                    isColumn = false
                )
            }
        }
    }
}

fun saveWorkout(log: Logs) {

}


