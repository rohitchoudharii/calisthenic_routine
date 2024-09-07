package com.calisthenics.routine.view.bulkdump

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calisthenics.routine.ViewModels.BulkDumpViewModel
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.ViewModels.RoutineViewModel
import com.calisthenics.routine.ViewModels.WorkoutViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.view.Home.HomeViewController
import com.calisthenics.routine.view.common.ScreenHeader

@Composable
fun BulkDataDumpController() {
    val mainController = rememberNavController()
    val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)
    val routineViewModel = ViewModelFactory().create(RoutineViewModel::class.java)
    val workflowViewModel = ViewModelFactory().create(WorkoutViewModel::class.java)
    val bulkDumpViewModel = BulkDumpViewModel(exerciseViewModel, routineViewModel, workflowViewModel)

    BackHandler {
        NavigationUtil.navigate(mainController, "Home")
    }
    NavHost(navController = mainController, startDestination = "Bulk Data Dump") {
        composable("Bulk Data Dump") {
            ScreenHeader(
                title = "Home",
                onBackButtonClick = {
                    NavigationUtil.navigate(
                        mainController, "Home"
                    )
                }) {
                BulkFilePicker(bulkDumpViewModel)
            }
        }
        composable("Home") {
            HomeViewController()
        }
    }
}

@Composable
fun BulkFilePicker(bulkDumpViewModel: BulkDumpViewModel) {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val contentResolver = context.contentResolver

    // Launching file picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            Toast.makeText(context, "File selected: ${uri.path}", Toast.LENGTH_LONG).show()
        }
    }

    Column {
        Button(
            onClick = {
                launcher.launch("*/*") // Change the mime type as needed
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Select File")
        }

        selectedFileUri?.let { uri ->
            Text(text = "Selected File: ${uri.path}")

            Button(onClick = {
                Toast.makeText(context, "Bulk upload Started", Toast.LENGTH_LONG).show()
                bulkDumpViewModel.dumpEntitiesFromFile(
                    contentResolver.openInputStream(
                        selectedFileUri!!
                    )
                )
                Toast.makeText(context, "Bulk upload completed", Toast.LENGTH_LONG).show()
            }) {
                Text("Perform Data Dump")
            }
        }


    }
}