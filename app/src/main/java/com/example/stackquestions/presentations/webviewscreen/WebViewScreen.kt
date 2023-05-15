package com.example.stackquestions.presentations.webviewscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WebViewScreen(url: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        WebViewComponent(url = url)
    }
}
