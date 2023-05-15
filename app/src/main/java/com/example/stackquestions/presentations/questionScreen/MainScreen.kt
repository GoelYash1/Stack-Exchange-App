package com.example.stackquestions.presentations.questionScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stackquestions.presentations.questionScreen.viewmodels.questionviewmodel.QuestionViewModel
import com.example.stackquestions.presentations.questionScreen.viewmodels.searchviewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(questionViewModel: QuestionViewModel, searchViewModel: SearchViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val refreshing by questionViewModel.refreshing.observeAsState()
    Scaffold(
        modifier = Modifier.padding(10.dp),
        topBar = {
            MainScreenTopBar(onSearch = { query ->
                searchQuery = query
                searchViewModel.searchQuery(query)
            })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            if (searchQuery.isEmpty()) {
                ManageSearchQueryEmpty(questionViewModel)
            } else {
                ManageSearchQueryNotEmpty(searchQuery, questionViewModel, searchViewModel)
            }
        }
    }
}


