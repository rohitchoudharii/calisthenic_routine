package com.calisthenics.routine

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HtmlContentDisplay(htmlContent: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            webViewClient = WebViewClient() // Handle links within the WebView
            loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        }
    })
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HtmlViewerScreen() {
    Box{
        // Example HTML content with <img> and <p> tags
        val htmlContent = """"""
        HtmlContentDisplay(htmlContent)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHtmlViewerScreen() {
    HtmlViewerScreen()
}