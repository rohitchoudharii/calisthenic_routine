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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.RoutineViewModel
import com.calisthenics.routine.ViewModels.WorkoutViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Routine
import com.calisthenics.routine.models.Workout
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent
import java.time.DayOfWeek
import java.util.UUID
import java.util.stream.Collectors

@Composable
fun WorkoutEditViewController(workout: Workout) {
    val workoutEditViewController = rememberNavController()
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)
    val workoutViewModel = ViewModelFactory().create(WorkoutViewModel::class.java)


    NavHost(
        navController = workoutEditViewController,
        startDestination = Screens.EDIT_WORKOUT.screen
    ) {
        composable(Screens.EDIT_WORKOUT.screen) {
            ScreenHeader(
                title = "Workout Home",
                onBackButtonClick = { NavigationUtil.navigate(workoutEditViewController, Screens.WORKOUT_PLANS.screen) }) {
                WorkoutEditView(
                    workout,
                    workoutEditViewController,
                    routineViewModel,
                    workoutViewModel
                )
            }
        }
        composable(Screens.WORKOUT_PLANS.screen) {
            WorkoutPlanViewController()
        }
    }
}

@Composable
fun WorkoutEditView(
    workout: Workout,
    workoutEditViewController: NavHostController,
    routineViewModel: RoutineViewModel,
    workoutViewModel: WorkoutViewModel
) {
    var allRoutines: MutableMap<UUID, Routine> = routineViewModel.getAllRoutines().stream().collect(
        Collectors.toMap({ it.id }, { it })
    )
    var selectedRoutines: MutableMap<UUID, Routine> = workout.routineIds.stream().collect(
        Collectors.toMap({ it }, { routineViewModel.getRoutine(it) })
    )

    var overlayRoutineSelector by remember { mutableStateOf(false) }

    BackHandler {
        if(overlayRoutineSelector){
            overlayRoutineSelector = false
        }else{
            NavigationUtil.navigate(workoutEditViewController, Screens.WORKOUT_PLANS.screen)
        }
    }

    Box(
        Modifier
            .padding(5.dp)
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        Column {
            RenderEditView(routineViewModel, workout.week, workout.routineIds) { overlayRoutineSelector = true }
            Button(onClick = {
                workoutViewModel.saveWorkout(workout)
                NavigationUtil.navigate(workoutEditViewController, Screens.WORKOUT_PLANS.screen)
            }) {
                Text("Save")
            }
        }
        if (overlayRoutineSelector) {
            Box(
                Modifier
                    .background(Color.Cyan)
                    .zIndex(1F)
                    .padding(20.dp)
                    .fillMaxSize(),
            )
            {
                Column {
                    LazyColumn {
                        items(allRoutines.values.toMutableList()) { routine ->
                            RenderAllRoutineInOverLay(
                                routine,
                                selectedRoutines.containsKey(routine.id)
                            ) { flag ->
                                if (flag) {
                                    selectedRoutines[routine.id] = routine
                                } else {
                                    selectedRoutines.remove(routine.id)
                                }
                                workout.routineIds = selectedRoutines.keys.toMutableList()
                            }
                        }
                    }
                    Button(onClick = { overlayRoutineSelector = false }) {
                        Text("Save")
                    }
                }

            }
        }
    }
}

@Composable
private fun RenderAllRoutineInOverLay(
    routine: Routine,
    flag: Boolean,
    onchange: (Boolean) -> Unit
) {
    var selectionFlag by remember {
        mutableStateOf(flag)
    }
    Row {
        TextDetailsComponent(
            text = routine.name,
            title = "Routines",
            fontMultiplier = 1F,
            isColumn = false
        )
        Checkbox(checked = selectionFlag, onCheckedChange = { nextFlag ->
            selectionFlag = nextFlag;
            onchange.invoke(selectionFlag)
        })
    }

}

@Composable
private fun RenderEditView(
    routineViewModel: RoutineViewModel,
    week: DayOfWeek,
    routines: MutableList<UUID>,
    enableRoutineSelector: () -> Unit
) {
    TextDetailsComponent(
        text = week.name,
        title = "Day",
        fontMultiplier = 1F,
        isColumn = false
    )
    TextDetailsComponent(
        text = "",
        title = "Routines",
        fontMultiplier = 1F,
        isColumn = false
    )
    RenderRoutines(routines.stream().map { routineViewModel.getRoutine(it) }.collect(Collectors.toList()))
    Button(onClick = { enableRoutineSelector.invoke() }) {
        Text("Add/Remove Routine")
    }
}


@Composable
fun RenderRoutines(routines: MutableList<Routine>) {
    var index = 0
    LazyColumn(Modifier.fillMaxHeight(0.5F)) {
        items(routines) { routine ->
            RenderRoutine(routine, ++index)
        }
    }
}

@Composable
fun RenderRoutine(routine: Routine, index: Int) {
    Box(
        Modifier
            .padding(5.dp)
            .background(Color.Gray)
            .fillMaxWidth()
    ) {
        TextDetailsComponent(
            text = routine.name,
            title = index.toString(),
            fontMultiplier = 1F,
            isColumn = false
        )
    }
}

