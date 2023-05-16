package com.example.stackquestions.presentations.filterScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun FilterScreen(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "A work in progress", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
    }
}