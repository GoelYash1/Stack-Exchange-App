package com.example.stackquestions.presentations.questionScreen

import androidx.compose.runtime.Composable
import com.example.stackquestions.SearchBar

@Composable
fun MainScreenTopBar(onSearch: (String) -> Unit) {
    SearchBar(onSearch = { onSearch(it.trim()) })
}