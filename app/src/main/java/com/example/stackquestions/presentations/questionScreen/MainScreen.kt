package com.example.stackquestions.presentations.questionScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel
import com.example.stackquestions.viewmodels.searchviewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(questionViewModel: QuestionViewModel, searchViewModel: SearchViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val refreshing by questionViewModel.refreshing.observeAsState()
    val ptrState = rememberPullRefreshState(refreshing = refreshing?:false, onRefresh = {questionViewModel.updateQuestions()})
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
                .pullRefresh(ptrState)
                .fillMaxWidth()
                .padding(it)
        ) {
            if (searchViewModel.selectedChips.value.isNullOrEmpty() && searchQuery.isEmpty()) {
                ManageSearchQueryEmpty(questionViewModel)
                PullRefreshIndicator(refreshing = refreshing?:false, state = ptrState, modifier = Modifier.align(Alignment.TopCenter))
            } else {
                ManageSearchQueryNotEmpty(questionViewModel, searchViewModel)
            }
        }
    }
}


