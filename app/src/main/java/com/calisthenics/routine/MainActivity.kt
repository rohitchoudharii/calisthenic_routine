package com.calisthenics.routine

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.calisthenics.routine.application.ObjectBoxApplication
import com.calisthenics.routine.ui.theme.CalisthenicsRoutineTheme
import com.calisthenics.routine.view.Home.HomeViewController
import java.io.InputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val boxStore = (application as ObjectBoxApplication)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalisthenicsRoutineTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    HomeViewController()
                }
            }
        }
    }
}

