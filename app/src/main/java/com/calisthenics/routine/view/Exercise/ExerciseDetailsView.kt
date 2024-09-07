package com.calisthenics.routine.view.Exercise

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.calisthenics.routine.ViewModels.ExerciseViewModel
import com.calisthenics.routine.common.NavigationUtil
import com.calisthenics.routine.config.ViewModelFactory
import com.calisthenics.routine.models.Exercise
import com.calisthenics.routine.view.common.ScreenHeader
import com.calisthenics.routine.view.common.TextDetailsComponent

@Composable
fun ExerciseDetailsController(exercise: Exercise) {
    val mainController = rememberNavController()
    val exerciseViewModel = ViewModelFactory().create(ExerciseViewModel::class.java)

    BackHandler {
        NavigationUtil.navigate(mainController, Screens.EXERCISE_HOME.screen)
    }

    NavHost(navController = mainController, startDestination = Screens.SHOW_EXERCISE.screen) {
        composable(Screens.SHOW_EXERCISE.screen) {
            ScreenHeader(
                title = "Back",
                onBackButtonClick = {
                    NavigationUtil.navigate(
                        mainController,
                        Screens.EXERCISE_HOME.screen
                    )
                }) {
                ExerciseDetailsView(
                    exerciseDetailsController = mainController,
                    exercise = exercise,
                    exerciseViewModel = exerciseViewModel
                )
            }
        }
        composable(Screens.EDIT_EXERCISE.screen) {
            ExerciseEditController(exercise = exercise)
        }
        composable(Screens.EXERCISE_HOME.screen) {
            ExerciseHomeViewController()
        }
    }
}


@Composable
fun ExerciseDetailsView(
    exerciseDetailsController: NavController,
    exercise: Exercise,
    exerciseViewModel: ExerciseViewModel,
) {
    Box {
        ExerciseCompleteDetails(
            exercise = exercise,
            exerciseDetailsController = exerciseDetailsController,
            exerciseViewModel
        )
    }
}

@Composable
fun ExerciseCompleteDetails(
    exercise: Exercise,
    exerciseDetailsController: NavController,
    exerciseViewModel: ExerciseViewModel
) {

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
                TextDetailsComponent(
                    exercise.name,
                    "Exercise name",
                    boxModifier = Modifier
                        .weight(0.5F)
                        .fillMaxWidth()
                        .padding(5.dp)
                )
                if (exercise.tags.isNotEmpty()) {
                    TextDetailsComponent(
                        exercise.tags.joinToString(", "),
                        "Tags",
                        boxModifier = Modifier
                            .weight(0.5F)
                            .fillMaxWidth()
                    )
                }

                if (exercise.description.isNotEmpty()) {
                    TextDetailsComponent(
                        exercise.description,
                        "Exercise description",
                        textModifier = Modifier
                            .verticalScroll(rememberScrollState()),
                        boxModifier = Modifier
                            .weight(3F)
                            .fillMaxWidth(),
                        showHtml = exercise.showDescriptionAsHtmlContent
                    )
                }
                if (exercise.referenceUrls.isNotEmpty()) {
                    ExerciseImageView(
                        exercise.referenceUrls,
                        modifier = Modifier
                            .weight(3F)
                            .fillMaxWidth()
                    )
                }
                ExerciseActionButtons(
                    exercise,
                    exerciseDetailsController,
                    exerciseViewModel,
                    boxModifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun ExerciseImageView(referenceUrls: List<String>, modifier: Modifier) {

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn {
            items(referenceUrls) { referenceUrl ->
                RenderImage(referenceUrl)
            }
        }
    }
}

@Composable
fun RenderImage(url: String, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .size(coil.size.Size.ORIGINAL)
            .decoderFactory(GifDecoder.Factory())
            .build()
    )
    Image(
        painter = painter,
        contentDescription = "test",
        modifier = modifier
            .height(250.dp)
            .width(500.dp)
    )
}

@Composable
private fun ExerciseActionButtons(
    exercise: Exercise,
    exerciseDetailsController: NavController,
    exerciseViewModel: ExerciseViewModel,
    boxModifier: Modifier
) {
    Box(boxModifier){
        Row {
            Button(
                onClick = {
                    Log.d("ExerciseHomeView", "Edit: $exercise")
                    NavigationUtil.navigate(exerciseDetailsController, Screens.EDIT_EXERCISE.screen)
                },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(text = "Edit Exercise")
            }
            Button(
                onClick = {
                    exerciseViewModel.delete(exercise)
                    NavigationUtil.navigate(exerciseDetailsController, Screens.EXERCISE_HOME.screen)
                },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(text = "Delete Exercise")
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun ExerciseCompleteDetailsPreview() {
}