package com.calisthenics.routine.view.Logs

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.view.Exercise.ExerciseDetailsController
import com.calisthenics.routine.view.Exercise.ExerciseEditController
import com.calisthenics.routine.view.Exercise.ExerciseHomeView
import com.calisthenics.routine.view.Exercise.Screens
import com.calisthenics.routine.view.Home.HomeViewController
import java.util.UUID

@Composable
fun LogHomeViewController(){
    val mainController = rememberNavController()
    val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)

    BackHandler {
        NavigationUtil.navigate(mainController, "Home")
    }

    NavHost(navController = mainController, startDestination = Screens.EXERCISE_HOME.screen) {
        composable(Screens.EXERCISE_HOME.screen){
            ExerciseHomeView(navController = mainController, exerciseViewModel = exerciseViewModel)
        }
        composable("${Screens.SHOW_EXERCISE.screen}/{exerciseObject}"){ backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseObject")
            val exercise = exerciseViewModel.getExercise(UUID.fromString(exerciseId))
            ExerciseDetailsController(exercise = exercise)
        }
        composable(Screens.CREATE_EXERCISE.screen){
            ExerciseEditController(exercise = Exercise())
        }
        composable("Home"){
            HomeViewController()
        }
    }
}