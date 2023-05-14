package com.example.stackquestions.presentations.questionScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.helpers.HelperFunctions
import com.example.stackquestions.presentations.questionScreen.viewmodels.QuestionViewModel
import com.example.stackquestions.util.Resource

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: QuestionViewModel) {
    val questions by viewModel.questions.observeAsState()
    val refreshing by viewModel.refreshing.observeAsState()
    val listState = rememberLazyListState()

    val mutableQuestionList = remember { mutableStateListOf<Question>() }

    when (questions) {
        is Resource.Success -> {
            val questionList = ((questions as Resource.Success<List<Question>>).data)
            if (questionList!=null){
                mutableQuestionList.addAll(questionList)

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(mutableQuestionList) { question ->
                        DisplayQuestionItemUI(question = question,viewModel)
                    }
                    if (viewModel.questions.value?.data?.isNotEmpty() == true && refreshing != true) {
                        item {
                            LoadMoreProgressBar(
                                isRefreshing = refreshing ?: false,
                                onLoadMore = {
                                    viewModel.updateQuestions()
                                }
                            )
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
                    mutableQuestionList.addAll(questionList)

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(mutableQuestionList) { question ->
                            DisplayQuestionItemUI(question, viewModel)
                        }
                        if (viewModel.questions.value?.data?.isNotEmpty() == true && refreshing != true) {
                            item {
                                LoadMoreProgressBar(
                                    isRefreshing = refreshing ?: false,
                                    onLoadMore = {
                                        viewModel.updateQuestions()
                                    }
                                )
                            }
                        }
                    }
                    Toast.makeText(LocalContext.current,"Loading Cached Questions",Toast.LENGTH_SHORT).show()
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


@Composable
fun LoadMoreProgressBar(
    isRefreshing: Boolean,
    onLoadMore: () -> Unit
) {
    if (isRefreshing) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            Button(onClick = onLoadMore) {
                Text("Load More")
            }
        }
    }
}



