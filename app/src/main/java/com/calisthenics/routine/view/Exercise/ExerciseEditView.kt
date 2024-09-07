package com.calisthenics.routine.view.Exercise

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.common.sanitiseString
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.view.common.EditTextBoxComponent
import com.calisthenics.routine.view.common.ScreenHeader
import java.util.UUID
import java.util.stream.Collectors

@Composable
fun ExerciseEditController(exercise: Exercise) {
    val mainController = rememberNavController()
    val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)


    BackHandler {
        NavigationUtil.navigate(mainController, Screens.SHOW_EXERCISE.screen)
    }
    NavHost(navController = mainController, startDestination = Screens.EDIT_EXERCISE.screen) {
        composable(Screens.EDIT_EXERCISE.screen) {
            ScreenHeader(
                title = "Exercise Details",
                onBackButtonClick = { NavigationUtil.navigate(mainController, Screens.SHOW_EXERCISE.screen) }) {
                ExerciseEditView(
                    exerciseEditController = mainController,
                    exercise = exercise,
                    exerciseViewModel = exerciseViewModel
                )
            }
        }
        composable(Screens.SHOW_EXERCISE.screen) {
            ExerciseDetailsController(exercise = exercise)
        }
    }
}

@Composable
fun ExerciseEditView(
    exerciseEditController: NavController,
    exercise: Exercise,
    exerciseViewModel: ExerciseViewModel = viewModel()
) {
    Box {
        ExerciseEditDetails(
            exercise = exercise,
            exerciseEditController = exerciseEditController,
            exerciseViewModel
        )
    }


}

@Composable
fun ExerciseEditDetails(
    exercise: Exercise,
    exerciseEditController: NavController,
    exerciseViewModel: ExerciseViewModel
) {
    var name by remember { mutableStateOf(exercise.name) }
    var description by remember { mutableStateOf(exercise.description) }
    var tags by remember { mutableStateOf(exercise.tags.joinToString("\n")) }
    var referenceUrls by remember { mutableStateOf(exercise.referenceUrls.joinToString("\n")) }

    Box(modifier = Modifier.padding(5.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .padding(5.dp)
                .background(color = Color.LightGray)
                .fillMaxSize()
        )
        {
            Column {
                EditTextBoxComponent(name, title = "Name") { nextString ->
                    name = nextString
                }
                EditTextBoxComponent(tags, title = "Tags") { nextString ->
                    tags = nextString
                }
                EditTextBoxComponent(
                    description,
                    title = "Description",
                    modifier = Modifier.fillMaxHeight(0.50f)
                ) { nextString ->
                    description = nextString
                }
                EditTextBoxComponent(referenceUrls, title = "ReferenceUrls") { nextString ->
                    referenceUrls = nextString
                }
                Button(onClick = {
                    Log.d("ExerciseEditView", "Old values: $exercise")
                    exercise.name = name
                    exercise.description = description.trim()
                    exercise.tags = sanitiseString(tags)
                    exercise.referenceUrls = sanitiseString(referenceUrls)

                    exerciseViewModel.saveExercise(exercise)
                    NavigationUtil.navigate(exerciseEditController, Screens.SHOW_EXERCISE.screen)

                    Log.d("ExerciseEditView", "New values: $exercise")
                }, modifier = Modifier.padding(5.dp)) {
                    Text(text = "Save")
                }
            }
        }
    }
}



