package com.calisthenics.routine.view.Routine

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.ViewModels.RoutineViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.common.dragContainer
import com.calisthenics.routine.common.draggableItems
import com.calisthenics.routine.common.rememberDragDropState
import com.calisthenics.routine.common.sanitiseString
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.ExerciseMetric
import com.calisthenics.routine.models.Routine
import com.calisthenics.routine.models.RoutineDetail
import com.calisthenics.routine.view.common.EditTextBoxComponent
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent

@Composable
fun RoutineEditViewController(routine: Routine) {
    val routineEditViewController = rememberNavController()
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)
    val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)

    NavHost(
        navController = routineEditViewController,
        startDestination = Screens.EDIT_ROUTINE.screen
    ) {
        composable(Screens.EDIT_ROUTINE.screen) {
            ScreenHeader(
                title = "Routine Details",
                onBackButtonClick = {
                    NavigationUtil.navigate(
                        routineEditViewController,
                        Screens.ROUTINE_DETAILS.screen
                    )
                }) {
                RoutineEditView(
                    routine,
                    routineViewModel,
                    exerciseViewModel,
                    routineEditViewController
                )
            }
        }
        composable(Screens.ROUTINE_DETAILS.screen) {
            RoutineDetailsViewController(routine)
        }
    }
}

@Composable
fun RoutineEditView(
    routine: Routine,
    routineViewModel: RoutineViewModel,
    exerciseViewModel: ExerciseViewModel,
    routineEditViewController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        RoutineEditDetails(
            routine,
            routineEditViewController,
            routineViewModel,
            exerciseViewModel
        )
    }

}

@Composable
fun RoutineEditDetails(
    routine: Routine,
    routineEditViewController: NavHostController,
    routineViewModel: RoutineViewModel,
    exerciseViewModel: ExerciseViewModel
) {
    val stateList = rememberLazyListState()
    var selectedRoutineDetails by remember { mutableStateOf(routine.routineDetails.toList()) }
    val selectedRoutineSize by remember { derivedStateOf { selectedRoutineDetails.size } }
    var allRoutineDetails = remember {
        exerciseViewModel.getAllExercises()
            .groupBy { if (it.tags.isNotEmpty()) it.tags[0] else "Miscellaneous" }
            .mapValues {
                val exercises = it.value
                exercises.map { exercise ->
                    val existingRoutineDetail =
                        selectedRoutineDetails.find { it.exerciseId == exercise.id }
                    existingRoutineDetail ?: RoutineDetail(exercise.id, ExerciseMetric())
                }

            }
    }
    var exerciseSelectionOverLay by remember { mutableStateOf(false) }

    BackHandler {
        if (exerciseSelectionOverLay) {
            exerciseSelectionOverLay = false
        } else {
            NavigationUtil.navigate(routineEditViewController, Screens.ROUTINE_DETAILS.screen)
        }
    }

    var name by remember { mutableStateOf(routine.name) }
    var tags by remember { mutableStateOf(routine.tags.joinToString("\n")) }

    val dragDropState =
        rememberDragDropState(
            lazyListState = stateList,
            draggableItemsNum = selectedRoutineSize,
            onMove = { fromIndex, toIndex ->
                selectedRoutineDetails = selectedRoutineDetails.toMutableList()
                    .apply { add(toIndex, removeAt(fromIndex)) }
            })

    fun addNewExerciseRoutineDetails(routineDetail: RoutineDetail) {
        selectedRoutineDetails = selectedRoutineDetails.toMutableList().apply { add(routineDetail) }
    }

    fun removeNewExerciseRoutineDetails(routineDetail: RoutineDetail) {
        selectedRoutineDetails = selectedRoutineDetails.toMutableList()
            .apply { removeIf { it.exerciseId == routineDetail.exerciseId } }
    }

    Box {
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
                    EditTextBoxComponent(
                        tags,
                        title = "Tags",
                        modifier = Modifier.fillMaxHeight(0.25f)
                    ) { nextString ->
                        tags = nextString
                    }
                    TextDetailsComponent(
                        text = "",
                        title = "Exercise Details",
                        fontSize = 10.sp
                    )
                    Box(Modifier.fillMaxHeight(0.9f)) {
                        LazyColumn(
                            modifier = Modifier.dragContainer(dragDropState),
                            state = stateList,
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            draggableItems(
                                items = selectedRoutineDetails,
                                dragDropState = dragDropState
                            ) { modifier, routineDetail ->
                                EditRoutineDetailView(
                                    routineDetail,
                                    exerciseViewModel = exerciseViewModel,
                                    modifier
                                )
                            }
                        }
                    }
                    RoutineActionButtons(
                        routine,
                        name,
                        tags,
                        routineViewModel,
                        routineEditViewController,
                        selectedRoutineDetails
                    ) {
                        Log.v("RoutineEditView", "Setting overlay")
                        exerciseSelectionOverLay = true
                    }
                }
            }
            if (exerciseSelectionOverLay) {
                Card(
                    modifier = Modifier
                        .background(Color.Gray)
                        .zIndex(1F)
                        .padding(20.dp)
                        .fillMaxSize(),
                ) {
                    Column() {
                        Box(
                            Modifier
                                .weight(9f)
                                .fillMaxWidth()
                        ) {
                            LazyColumn() {
                                items(allRoutineDetails.keys.toList()) { tagId ->
                                    val routineDetails = allRoutineDetails[tagId]
                                    DisplayRoutineDetailsByTagId(
                                        routineDetails,
                                        tagId,
                                        exerciseViewModel,
                                        selectedRoutineDetails,
                                        { addNewExerciseRoutineDetails(it) },
                                        { removeNewExerciseRoutineDetails(it) },
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = {
                                exerciseSelectionOverLay = false
                            },
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .weight(1f)
                        ) {
                            Text(text = "Back")
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun DisplayRoutineDetailsByTagId(
    routineDetails: List<RoutineDetail>?,
    tagId: String,
    exerciseViewModel: ExerciseViewModel,
    selectedRoutineDetails: List<RoutineDetail>,
    addNewExerciseRoutineDetails: (RoutineDetail) -> Unit = {},
    removeNewExerciseRoutineDetails: (RoutineDetail) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        Modifier.padding(horizontal = 0.dp, vertical = 2.dp)
    ) {
        Column {
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
                Box {
                    Column() {
                        for(routineDetail in routineDetails!!){
                            DisplayAllRoutineDetail(
                                routineDetail,
                                exerciseViewModel,
                                selectedRoutineDetails.any { it.exerciseId == routineDetail.exerciseId },
                                routineDetailSelectorMethod = { selectedExercise ->
                                    Log.v(
                                        "RoutineEditView",
                                        "Selected exercise: $selectedExercise"
                                    )
                                    addNewExerciseRoutineDetails(selectedExercise)
                                },
                                routineDetailDeselectMethod = { deselectedExercise ->
                                    Log.v(
                                        "RoutineEditView",
                                        "Removed exercise: $deselectedExercise"
                                    )
                                    removeNewExerciseRoutineDetails(deselectedExercise)
                                })
                        }
                    }
                }

            }
        }


    }
}

@Composable
private fun RoutineActionButtons(
    routine: Routine,
    name: String,
    tags: String,
    routineViewModel: RoutineViewModel,
    routineEditViewController: NavHostController,
    selectedRoutineDetails: List<RoutineDetail>,
    exerciseOverLayState: () -> Unit
) {
    Row {
        Button(onClick = { exerciseOverLayState.invoke() }) {
            Text("Select Exercise")
        }
        Button(onClick = {
            Log.d("RoutineEditView", "Old values: $routine")
            routine.name = name
            routine.tags = sanitiseString(tags)
            routine.routineDetails = selectedRoutineDetails.toMutableList()

            routineViewModel.saveRoutine(routine)
            NavigationUtil.navigate(routineEditViewController, Screens.ROUTINE_DETAILS.screen)

            Log.d("RoutineEditView", "New values: $routine")
        }, modifier = Modifier.padding(5.dp)) {
            Text(text = "Save")
        }
    }
}

@Composable
fun EditRoutineDetailView(
    routineDetail: RoutineDetail,
    exerciseViewModel: ExerciseViewModel,
    modifier: Modifier = Modifier
) {
    val exercise = exerciseViewModel.getExercise(routineDetail.exerciseId)

    var expanded by remember { mutableStateOf(false) }
    var exerciseSets by remember {
        mutableStateOf(routineDetail.exerciseMetric.sets.toString())
    }
    var exerciseReps by remember {
        mutableStateOf(routineDetail.exerciseMetric.repetations.toString())
    }
    var exerciseHolds by remember {
        mutableStateOf(routineDetail.exerciseMetric.holds.toString())
    }

    Box(modifier = modifier) {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextDetailsComponent(
                    text = exercise.name,
                    title = "Exercise Name",
                    fontSize = 15.sp,
                    fontMultiplier = 1F,
                    isColumn = false
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            if (expanded) {
                val numberFieldModifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(5.dp)
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(8.dp)) {
                    EditTextBoxComponent(
                        value = exerciseSets,
                        title = "Sets",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = numberFieldModifier,
                        applyDefaultModifier = false
                    ) { nextValue ->
                        exerciseSets = nextValue.replace("\n", "").trim()
                        if (exerciseSets.isNotEmpty()) {
                            routineDetail.exerciseMetric.sets = exerciseSets.toInt()
                        }
                    }
                    EditTextBoxComponent(
                        value = exerciseReps,
                        title = "Reps",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = numberFieldModifier,
                        applyDefaultModifier = false
                    ) { nextValue ->
                        exerciseReps = nextValue.replace("\n", "").trim()
                        if (exerciseReps.isNotEmpty()) {
                            routineDetail.exerciseMetric.repetations = exerciseReps.toInt()
                        }
                    }
                    EditTextBoxComponent(
                        value = exerciseHolds,
                        title = "Holds",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = numberFieldModifier,
                        applyDefaultModifier = false
                    ) { nextValue ->
                        exerciseHolds = nextValue.replace("\n", "").trim()
                        if (exerciseHolds.isNotEmpty()) {
                            routineDetail.exerciseMetric.holds = exerciseHolds.toInt()
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun DisplayAllRoutineDetail(
    routineDetail: RoutineDetail,
    exerciseViewModel: ExerciseViewModel,
    isRoutineDetailSelected: Boolean,
    routineDetailSelectorMethod: (RoutineDetail) -> Unit,
    routineDetailDeselectMethod: (RoutineDetail) -> Unit
) {
    val exercise = exerciseViewModel.getExercise(routineDetail.exerciseId)
    var isSelected by remember { mutableStateOf(isRoutineDetailSelected) }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { newSelection ->
                    isSelected = newSelection
                    if (isSelected) {
                        routineDetailSelectorMethod.invoke(routineDetail)
                    } else {
                        routineDetailDeselectMethod.invoke(routineDetail)
                    }
                })
            TextDetailsComponent(
                text = exercise.name,
                title = "Exercise",
                fontSize = 16.sp,
                fontMultiplier = 1F,
                isColumn = false
            )
        }
    }
}


