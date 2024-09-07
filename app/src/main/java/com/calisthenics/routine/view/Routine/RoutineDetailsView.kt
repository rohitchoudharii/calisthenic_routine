package com.calisthenics.routine.view.Routine

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.ViewModels.RoutineViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Routine
import com.calisthenics.routine.models.RoutineDetail
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent

@Composable
fun RoutineDetailsViewController(routine: Routine) {
    val mainController = rememberNavController()
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)
    val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)

    BackHandler {
        NavigationUtil.navigate(mainController, Screens.ROUTINE_HOME.screen)
    }

    NavHost(navController = mainController, startDestination = Screens.ROUTINE_DETAILS.screen) {
        composable(Screens.ROUTINE_DETAILS.screen) {
            ScreenHeader(
                title = "Routine Home",
                onBackButtonClick = { NavigationUtil.navigate(mainController, Screens.ROUTINE_HOME.screen) }) {
                RoutineDetailView(routine, mainController, routineViewModel, exerciseViewModel)
            }
        }
        composable(Screens.EDIT_ROUTINE.screen) {
            // RoutineEditViewController(routine)
            RoutineEditViewController(routine)
        }
        composable(Screens.ROUTINE_HOME.screen) {
            RoutineHomeViewController()
        }
    }
}

@Composable
fun RoutineDetailView(
    routine: Routine,
    routineDetailsController: NavHostController,
    routineViewModel: RoutineViewModel,
    exerciseViewModel: ExerciseViewModel
) {
    Box(modifier = Modifier.padding(5.dp)) {
        Column {
            TextDetailsComponent(
                text = routine.name,
                title = "Name"
            )
            if(routine.tags.isNotEmpty()){
                TextDetailsComponent(
                    text = routine.tags.joinToString("\n"),
                    title = "Tags"
                )
            }
            if(routine.description.isNotEmpty()){
                TextDetailsComponent(
                    text = routine.description, title = "Description", textModifier = Modifier
                        .padding(5.dp)
                        .fillMaxHeight(0.3F)
                        .verticalScroll(rememberScrollState())
                )
            }
            Text(
                text = "Exercises:",
                fontSize = 16.sp * 1.5,
                fontWeight = FontWeight.Bold,
            )
            LazyColumn(modifier = Modifier.fillMaxHeight(0.7F)) {
                items(routine.routineDetails.sortedBy { v -> v.order }) { routineDetails ->
                    RoutineDetailsView(routineDetails, exerciseViewModel)
                }
            }
            RoutineActionButtons(routine, routineDetailsController, routineViewModel)
        }
    }
}

@Composable
fun RoutineDetailsView(routineDetail: RoutineDetail, exerciseViewModel: ExerciseViewModel) {
    val exercise = exerciseViewModel.getExercise(routineDetail.exerciseId)
    val exerciseMetric = routineDetail.exerciseMetric

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .padding(5.dp)
            .background(color = Color.LightGray)
            .fillMaxSize()
    ) {
        Column {
            TextDetailsComponent(
                exercise.name,
                "Exercise name",
                fontSize = 15.sp,
                isColumn = false
            )
            if(exerciseMetric.sets != 0){
                TextDetailsComponent(
                    exerciseMetric.sets.toString(),
                    "Sets",
                    fontSize = 12.sp,
                    isColumn = false
                )
            }
            if(exerciseMetric.repetations != 0){
                TextDetailsComponent(
                    exerciseMetric.repetations.toString(),
                    "Reps",
                    fontSize = 12.sp,
                    isColumn = false
                )
            }

            if(exerciseMetric.holds != 0){
                TextDetailsComponent(
                    exerciseMetric.holds.toString(),
                    "Holds",
                    fontSize = 10.sp,
                    isColumn = false
                )
            }


        }
    }
}

@Composable
fun RoutineActionButtons(
    routine: Routine,
    routineDetailsController: NavHostController,
    routineViewModel: RoutineViewModel
) {
    Row {
        Button(
            onClick = {
                NavigationUtil.navigate(routineDetailsController, Screens.EDIT_ROUTINE.screen)
            },
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Text(text = "Edit Routine")
        }
        Button(
            onClick = {
                routineViewModel.deleteRoutine(routine)
                NavigationUtil.navigate(routineDetailsController, Screens.ROUTINE_HOME.screen)
            },
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Text(text = "Delete Routine")
        }
    }
}


