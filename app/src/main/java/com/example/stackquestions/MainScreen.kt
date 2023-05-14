package com.example.stackquestions

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.stackquestions.util.Resource
import com.example.stackquestions.viewmodels.QuestionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: QuestionViewModel) {
    val questions by viewModel.questions.observeAsState()
    val refreshing by viewModel.refreshing.observeAsState()
    when (questions) {
        is Resource.Success -> {
            val questionList = (questions as Resource.Success<List<Question>>).data
            if (questionList != null) {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(questionList) { index, question ->
                        DisplayQuestionItemUI(question = question, viewModel)
                        val itemCount = questionList.size
                        if (index == itemCount - 1) {
                            viewModel.loadMoreQuestions()
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
                    LazyColumn {
                        items(questionList) { question ->
                            DisplayQuestionItemUI(question, viewModel)
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayQuestionItemUI(
    question: Question,
    viewModel: QuestionViewModel
) {
    val minutesAgo = HelperFunctions.getTimeAgo(question.creation_date.toLong())
    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(15.dp))
            .fillMaxWidth(),
        content = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 24.dp)
                    .fillMaxWidth()
                ,
                content = {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .fillMaxWidth(0.2f)
                    ) {
                        question.owner.profile_image?.let { HelperFunctions.LoadImageFromUrl(imageUrl = it) }
                    }
                    Column(
                        Modifier
                            .fillMaxHeight()
                            .absoluteOffset(x = (-30).dp, y = 10.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                            .padding(
                                start = 40.dp,
                                end = 5.dp,
                                top = 30.dp
                            )
                            .zIndex(-1f),
                        content = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = question.owner.display_name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    overflow = TextOverflow.Ellipsis
                                )
                                FavouriteIcon(viewModel = viewModel, question = question)
                            }
                            Text(
                                text = "-$minutesAgo",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                            )
                            Text(
                                text = question.title,
                                fontSize = 15.sp,
                                lineHeight = 14.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(question.tags) { tag ->
                                    Text(
                                        text = "#$tag",
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp)
                            ) {
                                Text(
                                    text = "${question.score} votes",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(text = "${question.answer_count} answers",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                                Text(text = "${question.view_count} views",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun FavouriteIcon(
    viewModel: QuestionViewModel,
    question: Question
) {
    val context = LocalContext.current
    val isFavourite = question.is_favourite ?: false
    val icon = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val contentDescription = if (isFavourite) "removed from Favourites" else "added to Favourites"
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(20.dp)
            .clickable(true, onClick = {
                question.is_favourite = !isFavourite
                viewModel.favoriteQuestion(question, question.is_favourite!!)
                Toast
                    .makeText(
                        context,
                        "This question has been $contentDescription",
                        Toast.LENGTH_LONG
                    )
                    .show()
            })
    )
}

