package com.calisthenics.routine.view.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    title: String, onBackButtonClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(title = {
        Text(
            text = title, textAlign = TextAlign.Center, color = Color.Black
        )
    },
        navigationIcon = {
            IconButton(onClick = { onBackButtonClick.invoke() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
        },
        actions = actions)
}


@Composable
fun ScreenHeader(title: String, onBackButtonClick: () -> Unit = {}, content: @Composable () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopBarComponent(title = title, onBackButtonClick = onBackButtonClick)
        }
    ) { padding ->
        Box(Modifier.padding(padding)){
            content.invoke()
        }
    }
}

@Preview
@Composable
fun commonHeaderPreview(){
    //HomeScreen()
}

