package com.calisthenics.routine.view.Workout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.calisthenics.routine.models.Workout
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent
import java.time.DayOfWeek


@Composable
fun WorkoutPlanViewController() {
    val mainController = rememberNavController()
    val workoutViewModel = ViewModelFactory().create(WorkoutViewModel::class.java)
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)

    BackHandler {
        NavigationUtil.navigate( mainController, Screens.WORKOUT_HOME.screen)
    }

    NavHost(navController = mainController, startDestination = Screens.WORKOUT_PLANS.screen) {
        composable(Screens.WORKOUT_PLANS.screen) {
            ScreenHeader(
                title = "Workout Home",
                onBackButtonClick = { NavigationUtil.navigate( mainController, Screens.WORKOUT_HOME.screen) }) {
                WorkoutPlanView(mainController, workoutViewModel, routineViewModel)
            }
        }
        composable(Screens.EDIT_WORKOUT.screen + "/{weekId}") { backStackEntry ->
            val _week = backStackEntry.arguments?.getString("weekId")

            val workout = workoutViewModel.getWorkoutByWeek(DayOfWeek.valueOf(_week!!))
            WorkoutEditViewController(workout)

        }
        composable(Screens.WORKOUT_HOME.screen) {
            WorkoutHomeViewController()
        }
    }
}

@Composable
fun WorkoutPlanView(
    workoutPlanViewController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    routineViewModel: RoutineViewModel
) {

    var workouts: List<Workout> = workoutViewModel.getAllWorkouts()

    Box {
        LazyColumn {
            items(workouts.sortedBy { w -> w.week }) { workout ->
                LoadWorkout(workout, workoutPlanViewController, routineViewModel)
            }
        }
    }
}

@Composable
private fun LoadWorkout(
    workout: Workout,
    workoutPlanViewController: NavHostController,
    routineViewModel: RoutineViewModel
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .padding(5.dp)
            .background(color = Color.LightGray)
            .fillMaxWidth()
    )
    {
        Column {
            TextDetailsComponent(
                text = workout.week.name,
                title = "Day",
                fontMultiplier = 1F,
                isColumn = false
            )
            LoadRoutines(workout, routineViewModel)
            Button(onClick = { NavigationUtil.navigate(workoutPlanViewController, Screens.EDIT_WORKOUT.screen + "/${workout.week.name}") }) {
                Text(text = "Edit Workout")
            }
        }
    }
}


@Composable
private fun LoadRoutines(workout: Workout, routineViewModel: RoutineViewModel) {
    Column(Modifier.padding(5.dp)) {
        workout.routineIds.forEach { routineId ->
            val routine = routineViewModel.getRoutine(routineId)
            TextDetailsComponent(
                text = routine.name,
                title = "Routine",
                fontMultiplier = 1F,
                isColumn = false
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WorkoutPlanPreview() {
    WorkoutPlanViewController()
}
