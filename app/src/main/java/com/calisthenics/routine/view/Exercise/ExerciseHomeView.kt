package com.calisthenics.routine.view.Exercise

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.view.Home.HomeViewController
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent
import java.util.UUID

@Composable
fun ExerciseHomeViewController() {
    val mainController = rememberNavController()
    val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)

    BackHandler {
        NavigationUtil.navigate(mainController, "Home")
    }

    NavHost(navController = mainController, startDestination = Screens.EXERCISE_HOME.screen) {
        composable(Screens.EXERCISE_HOME.screen) {
            ScreenHeader(
                title = "Home",
                onBackButtonClick = { NavigationUtil.navigate(mainController, "Home") }) {
                ExerciseHomeView(
                    navController = mainController,
                    exerciseViewModel = exerciseViewModel
                )
            }
        }
        composable("${Screens.SHOW_EXERCISE.screen}/{exerciseId}") { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId")
            val exercise = exerciseViewModel.getExercise(UUID.fromString(exerciseId))
            ExerciseDetailsController(exercise = exercise)
        }
        composable(Screens.CREATE_EXERCISE.screen) {
            ExerciseEditController(exercise = Exercise())
        }
        composable("Home") {
            HomeViewController()
        }
    }
}

@Composable
fun ExerciseHomeView(
    exerciseViewModel: ExerciseViewModel = viewModel(),
    navController: NavHostController,
) {
    val exercisesByTagId: Map<String, List<Exercise>> =
        exerciseViewModel.getAllExercises().groupBy {
            if (it.tags.isNotEmpty()) it.tags[0] else "Miscellinious"
        }

    Box {
        Column {
            Box(Modifier.weight(15f)) {
                LazyColumn() {
                    items(exercisesByTagId.keys.toList()) { tagId ->
                        val exercises = exercisesByTagId[tagId]
                        ViewIndividualTags(tagId, exercises, navController)
                    }
                }
            }

            Button(
                onClick = {
                    NavigationUtil.navigate(navController, Screens.CREATE_EXERCISE.screen)
                },
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1F)
            ) {
                Text(text = "Create Exercise")
            }
        }
    }


}

@Composable
private fun ViewIndividualTags(
    tagId: String,
    exercises: List<Exercise>?,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Column() {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                TextDetailsComponent(text = "", title = tagId, boxModifier = Modifier.weight(9f))
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.weight(1f)
                )
            }
            if (expanded) {
                ViewExerciseCards(exercises!!, navController)
            }
        }
    }
}

@Composable
private fun ViewExerciseCards(
    exercises: List<Exercise>,
    navController: NavHostController
) {
    Box {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            for (exercise in exercises) {
                ExerciseBasicDetails(navController, exercise = exercise)
            }
        }
    }
}

@Composable
fun ExerciseBasicDetails(navController: NavHostController, exercise: Exercise) {
    Box(modifier = Modifier.padding(5.dp)) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .padding(5.dp)
                .background(color = Color.LightGray)
                .fillMaxWidth()
        )
        {
            Row(Modifier.clickable {
                Log.d("ExerciseHomeView", "Clicked: $exercise")
                navController.navigate("${Screens.SHOW_EXERCISE.screen}/${exercise.id}")
            }) {
                Box(Modifier.weight(9f)) {
                    TextDetailsComponent(
                        exercise.name,
                        "Name",
                        isColumn = false
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "next",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

