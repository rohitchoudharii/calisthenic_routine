package com.calisthenics.routine.view.Workout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.RoutineViewModel
import com.calisthenics.routine.ViewModels.WorkoutViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Logs
import com.calisthenics.routine.models.Workout
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent
import java.time.LocalDate
import java.util.UUID

@Composable
fun StartNewWorkoutController() {
    val startNewWorkoutController = rememberNavController()
    val workoutViewModel = ViewModelFactory().create(WorkoutViewModel::class.java)
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)
    val workout = workoutViewModel.getWorkoutByWeek(LocalDate.now().dayOfWeek)

    BackHandler {
        NavigationUtil.navigate(startNewWorkoutController, Screens.WORKOUT_HOME.screen)
    }

    NavHost(
        navController = startNewWorkoutController,
        startDestination = Screens.START_NEW_WORKOUT.screen
    ) {
        composable(Screens.START_NEW_WORKOUT.screen) {
            ScreenHeader(
                title = "Workout Home",
                onBackButtonClick = {
                    NavigationUtil.navigate(startNewWorkoutController, Screens.WORKOUT_HOME.screen) }) {
                TodaysRoutine(workout, startNewWorkoutController, routineViewModel)
            }
        }
        composable(Screens.WORKOUT_ROUTINE.screen + "/{routineId}") { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routineId")
            var routineId = workout.routineIds.first { r -> r == UUID.fromString(routeId) }
            val log = Logs(routine = routineViewModel.getRoutine(routineId))
            WorkoutRoutineViewController(log, 0)
        }
        composable(Screens.WORKOUT_HOME.screen) {
            WorkoutHomeViewController()
        }
    }
}

@Composable
fun TodaysRoutine(
    workout: Workout,
    startNewWorkoutController: NavHostController,
    routineViewModel: RoutineViewModel
) {
    Box {
        Column {
            TextDetailsComponent(
                text = "",
                title = workout.week.name
            )
            Box {
                LazyColumn {
                    items(workout.routineIds) { routineId ->
                        val routine = routineViewModel.getRoutine(routineId)
                        Box(
                            Modifier
                                .padding(5.dp)
                                .background(Color.LightGray)
                                .fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextDetailsComponent(
                                    text = routine.name,
                                    title = "Routines",
                                    fontMultiplier = 1F,
                                    isColumn = false
                                )
                                Button(onClick = {
                                    NavigationUtil.navigate(startNewWorkoutController, Screens.WORKOUT_ROUTINE.screen + "/${routine.id}")}) {
                                    Text(text = "Start The exercise")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun StartNewWorkoutPreview() {
    StartNewWorkoutController()
}
