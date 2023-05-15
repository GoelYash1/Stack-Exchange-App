package com.example.stackquestions.presentations.questionScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel
import com.example.stackquestions.viewmodels.searchviewmodel.SearchViewModel
import com.example.stackquestions.util.Resource

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManageSearchQueryEmpty(
    questionViewModel: QuestionViewModel
) {
    val questions by questionViewModel.questions.observeAsState()
    when (questions) {
        is Resource.Success -> {
            val questionList = ((questions as Resource.Success<List<Question>>).data)
            if (questionList!=null){
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(questionList) { question ->
                        DisplayQuestionItemUI(question = question,questionViewModel)
                    }
                }
            }
        }
        is Resource.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Error -> {
            val questionList = (questions as? Resource.Error<List<Question>>)?.data
            if (questionList != null) {
                if (questionList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(questionList) { question ->
                            DisplayQuestionItemUI(question, questionViewModel)
                        }
                    }
                    Toast.makeText(LocalContext.current,"Loading Cached Questions", Toast.LENGTH_SHORT).show()
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Error loading questions. Please connect to Internet and reload.",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                        (questions as? Resource.Error<List<Question>>)?.error?.message?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "No questions to show.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManageSearchQueryNotEmpty(
    questionViewModel: QuestionViewModel,
    searchViewModel: SearchViewModel
) {
    val filteredQuestions by searchViewModel.filteredQuestions.observeAsState()
    when (filteredQuestions) {
        is Resource.Success -> {
            val questionList = ((filteredQuestions as Resource.Success<List<Question>>).data)
            if (questionList!=null){
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(questionList) { question ->
                        DisplayQuestionItemUI(question = question,questionViewModel)
                    }
                }
            }
        }
        is Resource.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Error ->{
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Error loading questions.",
                    textAlign = TextAlign.Center
                )
            }
        }
        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Did Not match any Search Results :(",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}