package com.example.stackquestions.presentations.webviewscreen

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewComponent(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // WebView settings can be configured here
                settings.javaScriptEnabled = true
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}