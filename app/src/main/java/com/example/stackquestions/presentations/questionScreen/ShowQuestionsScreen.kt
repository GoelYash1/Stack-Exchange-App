package com.example.stackquestions.presentations.questionScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.Resource
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel
import com.example.stackquestions.viewmodels.searchviewmodel.SearchViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ManageSearchQueryEmpty(
    questionViewModel: QuestionViewModel
) {
    val questions by questionViewModel.questions.observeAsState()

    when (questions) {
        is Resource.Success -> {
            val questionList = (questions as Resource.Success<List<Question>>).data
            if (questionList != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(questionList) { index, question ->
                        DisplayQuestionItemUI(question = question, questionViewModel)

                        // Show ad after every fifth element
                        if ((index + 1) % 5 == 0 && index != questionList.lastIndex) {
                            AdComponent()
                        }
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
    var selectedTags by remember { mutableStateOf(searchViewModel.selectedChips.value ?: emptySet()) }

    Column(Modifier.fillMaxWidth()) {
        if (selectedTags.isNotEmpty()) {
            Button(onClick = {
                selectedTags = emptySet() // Clear the selectedTags
                searchViewModel.selectedChips.value?.clear()
                questionViewModel.updateQuestions()
            }, shape = CircleShape) {
                Text(text = "Clear Filters")
            }
        }
        when (filteredQuestions) {
            is Resource.Success -> {
                val questionList = (filteredQuestions as Resource.Success<List<Question>>).data
                val filteredList = if (selectedTags.isNotEmpty()) {
                    questionList?.filter { question ->
                        question.tags.any { selectedTags.contains(it) }
                    }
                } else {
                    questionList
                }

                if (!filteredList.isNullOrEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(filteredList) { index, question ->
                            DisplayQuestionItemUI(question = question, questionViewModel)
                            if ((index + 1) % 5 == 0 && index != filteredList.lastIndex) {
                                AdComponent()
                            }
                        }
                    }
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "No matching search results found :(",
                            textAlign = TextAlign.Center
                        )
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
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Error occurred while loading questions.\n${searchViewModel.errorMessage.value}",
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
                        text = "No matching search results found :(",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun AdComponent() {
    val context = LocalContext.current
    val adRequest = remember { AdRequest.Builder().build() }
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                setAdSize(AdSize.BANNER)
                loadAd(adRequest)
            }
        }
    )
}
