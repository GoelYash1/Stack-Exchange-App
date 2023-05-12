package com.example.stackquestions

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.stackquestions.models.Question
import com.example.stackquestions.util.Resource
import com.example.stackquestions.viewmodels.QuestionViewModel

@Composable
fun MainScreen(
    viewModel: QuestionViewModel
) {
    val questionList = remember { mutableStateListOf<Question>() }
    LaunchedEffect(Unit) {
        viewModel.questions.observeForever { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { questionResponse ->
                        questionList.clear()
                        questionList.addAll(questionResponse.items)
                    }
                }
                else -> Log.d("No data is there","Hello ")
            }
        }
    }
    if (questionList.isNotEmpty()) {
        LazyColumn {
            items(questionList) { question ->
                DisplayQuestionItemUI(question)
            }
        }
    } else {
        Text(text = "No questions found")
    }
}

@Composable
fun DisplayQuestionItemUI(
    question: Question
) {
    Column {
        Text(text = question.title)
    }
}
