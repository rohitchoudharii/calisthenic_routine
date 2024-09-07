package com.calisthenics.routine.view.Workout

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.view.Home.HomeViewController
import com.calisthenics.routine.view.common.ScreenHeader

@Composable
fun WorkoutHomeViewController() {
    val mainController = rememberNavController()

    BackHandler {
        NavigationUtil.navigate(mainController, "Home" )
    }

    NavHost(navController = mainController, startDestination = Screens.WORKOUT_HOME.screen) {
        composable(Screens.WORKOUT_HOME.screen) {
            ScreenHeader(
                title = "Main Home",
                onBackButtonClick = { NavigationUtil.navigate(mainController, "Home" )}) {
                WorkoutHomeView(mainController)
            }
        }
        composable(Screens.START_NEW_WORKOUT.screen) {
            StartNewWorkoutController()
        }
        composable(Screens.WORKOUT_PLANS.screen) {
            WorkoutPlanViewController()
        }
        composable("Home") {
            HomeViewController()
        }
    }
}

@Composable
fun WorkoutHomeView(workoutHomeNavigation: NavHostController) {
    Box(Modifier.padding(10.dp)) {
        Column {
            Button(
                onClick = { NavigationUtil.navigate(workoutHomeNavigation, Screens.WORKOUT_PLANS.screen) },
                Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Text("Workout Plans")
            }
            Button(
                onClick = { NavigationUtil.navigate(workoutHomeNavigation, Screens.START_NEW_WORKOUT.screen) },
                Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Text("Start a new workout")
            }
        }
    }
}

@Preview
@Composable
fun WorkoutHomePreview() {
    WorkoutHomeViewController()
}
