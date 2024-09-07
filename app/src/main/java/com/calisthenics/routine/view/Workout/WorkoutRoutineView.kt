package com.calisthenics.routine.view.Workout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.models.ExerciseMetric
import com.calisthenics.routine.models.LogDetail
import com.calisthenics.routine.models.Logs
import com.calisthenics.routine.view.Exercise.ExerciseImageView
import com.calisthenics.routine.view.common.EditTextBoxComponent
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent

@Composable
fun WorkoutRoutineViewController(log: Logs, index: Int) {
    val workoutRoutineViewController = rememberNavController()

    NavHost(
        navController = workoutRoutineViewController,
        startDestination = Screens.WORKOUT_ROUTINE.screen
    ) {
        composable(Screens.WORKOUT_ROUTINE.screen) {
            ScreenHeader(
                title = "Main Home",
                onBackButtonClick = { NavigationUtil.navigate(workoutRoutineViewController, Screens.WORKOUT_HOME.screen) }) {
                WorkoutRoutineView(log = log, index, workoutRoutineViewController)
            }
        }
        composable(Screens.WORKOUT_ROUTINE.screen + "/{nextIndex}") { backStackEntry ->
            val nextIndex = backStackEntry.arguments?.getString("nextIndex")
            WorkoutRoutineViewController(log, nextIndex!!.toInt())
        }
        composable(Screens.FINISH_WORKOUT.screen) {
            CompleteWorkoutViewController(log)
        }
        composable(Screens.WORKOUT_HOME.screen) {
            WorkoutHomeViewController()
        }
    }
}

@Composable
fun WorkoutRoutineView(
    log: Logs,
    index: Int,
    workoutRoutineViewController: NavHostController,

) {


    var overlayExerciseMatrix by remember { mutableStateOf(false) }

    BackHandler {
        if(overlayExerciseMatrix){
            overlayExerciseMatrix = false
        }else{
            NavigationUtil.navigate(workoutRoutineViewController, Screens.WORKOUT_HOME.screen)
        }
    }

    val routine = log.routine
    val routineDetails = routine.routineDetails
    val routineDetail = routineDetails[index]
    val targetExerciseMatrix = routineDetail.exerciseMetric
    val exercise = exerciseViewModel.getExercise(routineDetails[index].exerciseId)
    val logDetail = LogDetail(routineDetail = routineDetail)

    var exerciseMetrics = remember { logDetail.completedExerciseMetrics.toMutableStateList() }

    Box(
        Modifier
            .padding(5.dp)
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Column {
            TextDetailsComponent(
                text = exercise.name,
                title = "Exercise",
                boxModifier = Modifier.weight(1f)
            )
            if(exercise.description.isNotEmpty()){
                TextDetailsComponent(
                    text = exercise.description, title = "How to perform exercise",
                    textModifier = Modifier
                        .padding(5.dp)
                        .fillMaxHeight(getDetailsComponentHeight(exercise))
                        .verticalScroll(rememberScrollState()),
                    showHtml = exercise.showDescriptionAsHtmlContent,
                    boxModifier = Modifier.weight(if(exercise.referenceUrls.isEmpty()) 8f else 4f)
                )
            }
            if(exercise.referenceUrls.isNotEmpty()){
                ExerciseImageView(exercise.referenceUrls, modifier = Modifier.fillMaxHeight(
                    getImageComponentHeight(exercise)))
            }

            Row(Modifier.weight(1f)) {
                Button(onClick = {
                    overlayExerciseMatrix = true
                }) {
                    Text(text = "Log set")
                }
                if (routineDetails.size - 1 > index) {
                    Button(onClick = {
                        logDetail.completedExerciseMetrics = exerciseMetrics.toMutableList()
                        log.logDetails.add(logDetail)
                        NavigationUtil.navigate(workoutRoutineViewController,Screens.WORKOUT_ROUTINE.screen + "/${index + 1}")
                    }) {
                        Text(text = "Next")
                    }
                } else {
                    Button(onClick = {
                        logDetail.completedExerciseMetrics = exerciseMetrics.toMutableList()
                        log.logDetails.add(logDetail)
                        NavigationUtil.navigate(workoutRoutineViewController,Screens.FINISH_WORKOUT.screen)
                    }) {
                        Text(text = "Finish")
                    }
                }
            }
        }

        if (overlayExerciseMatrix) {
            var exerciseMetric = ExerciseMetric()

            Box(
                Modifier
                    .fillMaxSize()
                    .zIndex(0.9F)
                    .padding(25.dp)
                    .background(Color.White)
                    .clickable(false) {}
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .zIndex(1F)
                        .background(Color.Red.copy(alpha = 0.5f))
                        .clickable(true) {}
                ) {
                    Column(Modifier.padding(5.dp)) {
                        TargetExerciseMetrix(targetExerciseMatrix)


                        if (exerciseMetrics.isNotEmpty()) {
                            TextDetailsComponent(
                                text = "",
                                title = "Completed Sets"
                            )
                            LazyColumn {
                                var i = 0
                                items(exerciseMetrics) { completedExerciseMetric ->
                                    RenderCompletedLogDetail(completedExerciseMetric, ++i)
                                }
                            }
                        }

                        AddEditTextBox("Reps") { newVal ->
                            exerciseMetric.repetations = newVal.toInt()
                        }
                        AddEditTextBox("Holds") { newVal ->
                            exerciseMetric.holds = newVal.toInt()
                        }
                        Button(onClick = {
                            if (exerciseMetric.holds != 0 || exerciseMetric.repetations != 0)
                                exerciseMetrics.add(exerciseMetric)
                            overlayExerciseMatrix = false
                        }) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}

fun getDetailsComponentHeight(exercise: Exercise): Float {
    var maxHeight = 0.8f
    if(exercise.referenceUrls.isNotEmpty()){
        maxHeight = 0.3f
    }
    return maxHeight
}

fun getImageComponentHeight(exercise: Exercise): Float {
    var maxHeight = 0.8f
    if(exercise.description.isNotEmpty()){
        maxHeight = 0.5f
    }
    return maxHeight
}

@Composable
private fun TargetExerciseMetrix(targetExerciseMatrix: ExerciseMetric) {
    Box(Modifier.padding(5.dp)) {
        Column {
            TextDetailsComponent(
                text = "",
                title = "Target",
            )
            TextDetailsComponent(
                text = targetExerciseMatrix.sets.toString(),
                title = "Sets",
                fontMultiplier = 1F
            )
            if (targetExerciseMatrix.repetations != 0) {
                TextDetailsComponent(
                    text = targetExerciseMatrix.repetations.toString(),
                    title = "Reps",
                    fontMultiplier = 1F
                )
            }
            if (targetExerciseMatrix.holds != 0) {
                TextDetailsComponent(
                    text = targetExerciseMatrix.holds.toString(),
                    title = "Holds",
                    fontMultiplier = 1F
                )
            }
        }
    }

}

@Composable
fun AddEditTextBox(s: String, onChange: (String) -> Unit) {
    var value by remember { mutableStateOf("0") }

    EditTextBoxComponent(
        value = value,
        title = s,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    ) { newVal ->
        value = newVal.replace("\n", "").trim()
        if (value.isNotEmpty()) {
            onChange.invoke(value)
        }
    }
}

@Composable
fun RenderCompletedLogDetail(exerciseMetric: ExerciseMetric, i: Int) {
    Box(
        Modifier
            .padding(0.5.dp)
            .background(Color.Cyan.copy(alpha = 0.5f))
            .fillMaxWidth()
    ) {
        Column {
            TextDetailsComponent(
                text = "",
                title = "Sets $i",
                fontMultiplier = 1F,
                isColumn = false
            )
            if (exerciseMetric.repetations != 0) {
                TextDetailsComponent(
                    text = exerciseMetric.repetations.toString(),
                    title = "Repetations",
                    fontMultiplier = 1F,
                    isColumn = false
                )
            }
            if (exerciseMetric.holds != 0) {
                TextDetailsComponent(
                    text = exerciseMetric.holds.toString(),
                    title = "Holds",
                    fontMultiplier = 1F,
                    isColumn = false
                )
            }

        }

    }
}


