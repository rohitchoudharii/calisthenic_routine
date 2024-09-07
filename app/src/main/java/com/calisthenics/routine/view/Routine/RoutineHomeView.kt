package com.calisthenics.routine.view.Routine

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.calisthenics.routine.ViewModels.RoutineViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Routine
import com.calisthenics.routine.view.Home.HomeViewController
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent
import java.util.UUID

@Composable
fun RoutineHomeViewController() {
    val mainController = rememberNavController()
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)

    BackHandler {
        NavigationUtil.navigate(mainController, "Home")
    }

    NavHost(navController = mainController, startDestination = Screens.ROUTINE_HOME.screen) {
        composable(Screens.ROUTINE_HOME.screen) {
            ScreenHeader(
                title = "Home",
                onBackButtonClick = { NavigationUtil.navigate(mainController, "Home") }) {
                RoutineHomeView(routineViewModel, mainController)
            }
        }
        composable(Screens.CREATE_ROUTINE.screen) {
            // RoutineEditViewController(Routine())
            RoutineEditViewController(Routine())
        }
        composable(Screens.ROUTINE_DETAILS.screen + "/{routineId}") { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routineId")
            val routine = routineViewModel.getRoutine(UUID.fromString(routineId))
            RoutineDetailsViewController(routine)
        }
        composable("Home") {
            HomeViewController()
        }
    }
}


@Composable
fun RoutineHomeView(
    routineViewModel: RoutineViewModel = viewModel(),
    routineHomeController: NavController
) {
    val routines: List<Routine> = routineViewModel.getAllRoutines()
    Box {
        Column {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight(fraction = 0.9F)
            ) {
                items(routines) { routine ->
                    RoutineBasicDetails(routineHomeController, routine = routine)
                }
            }
            Button(
                onClick = {
                    NavigationUtil.navigate(routineHomeController, Screens.CREATE_ROUTINE.screen)
                },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(text = "Create Routine")
            }
        }
    }
}

@Composable
fun RoutineBasicDetails(routineHomeController: NavController, routine: Routine) {
    Box(modifier = Modifier.padding(5.dp)) {
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
                    routine.name,
                    "Routine name"
                )
                TextDetailsComponent(
                    routine.tags.joinToString(", "),
                    "Label"
                )
                Button(
                    onClick = {
                        NavigationUtil.navigate(routineHomeController, "${Screens.ROUTINE_DETAILS.screen}/${routine.id}")
                    },
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text(text = "Details")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExerciseHomePreview() {
    RoutineHomeViewController()
}