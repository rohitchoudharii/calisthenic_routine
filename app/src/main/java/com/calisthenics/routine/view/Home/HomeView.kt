package com.calisthenics.routine.view.Home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.view.bulkdump.BulkDataDumpController
import com.calisthenics.routine.view.Exercise.ExerciseHomeViewController
import com.calisthenics.routine.view.Logs.LogHomeViewController
import com.calisthenics.routine.view.Routine.RoutineHomeViewController
import com.calisthenics.routine.view.Workout.WorkoutHomeViewController


@Composable
fun HomeViewController(){
    val mainController = rememberNavController()
    BackHandler {

    }

    NavHost(navController = mainController, startDestination = Screens.HOME.screen) {
        composable(Screens.HOME.screen){
            HomeView(navController = mainController)
        }
        composable(Screens.EXERCISE.screen){
            ExerciseHomeViewController()
        }
        composable(Screens.ROUTINE.screen){
            RoutineHomeViewController()
        }
        composable(Screens.WORKOUT.screen){
            WorkoutHomeViewController()
        }
        composable(Screens.LOGS.screen){
            LogHomeViewController()
        }
        composable("Bulk Data Dump"){
            BulkDataDumpController()
        }

    }
}

@Composable
fun HomeView(navController: NavHostController) {

    val modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
    Box{
        Column{
            Box(Modifier.fillMaxHeight(0.3f)){}
            Button(onClick = { NavigationUtil.navigate(navController, Screens.EXERCISE.screen) } , modifier = modifier) {
                Text("Exercise")
            }
            Button(onClick = { NavigationUtil.navigate(navController, Screens.ROUTINE.screen) }, modifier = modifier) {
                Text("Routine")
            }
            Button(onClick = { NavigationUtil.navigate(navController, Screens.WORKOUT.screen) }, modifier = modifier) {
                Text("Workout")
            }
            Button(onClick = { NavigationUtil.navigate(navController, Screens.LOGS.screen) }, modifier = modifier) {
                Text("Logs")
            }
            Button(onClick = { NavigationUtil.navigate(navController, "Bulk Data Dump") }, modifier = modifier) {
                Text("Bulk data dump")
            }
        }
    }
}

@Composable
@Preview
fun HomeViewPreview(){
    HomeViewController()
}