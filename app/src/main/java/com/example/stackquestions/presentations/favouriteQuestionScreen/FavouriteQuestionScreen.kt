package com.example.stackquestions.presentations.favouriteQuestionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.presentations.webviewscreen.WebViewScreen
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FavouriteQuestionScreen(questionViewModel: QuestionViewModel) {
    val favouriteQuestions by questionViewModel.favoriteQuestions.observeAsState()
    val confirmDeleteItem = remember { mutableStateOf<Question?>(null) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "FAVOURITES", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Cyan)
        if (favouriteQuestions.isNullOrEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "NOTHING TO SHOW",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Add Question to your favourites to get to know about their activity",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            // Display the list of favorite questions
            Column(Modifier.fillMaxWidth()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.error, RoundedCornerShape(10.dp)),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Swipe an item to remove from favorites",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
                LazyColumn {
                    items(favouriteQuestions!!) { item ->
                        val currentItem by rememberUpdatedState(item)
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                confirmDeleteItem.value = item
                                false
                            }
                        )
                        SwipeToDismiss(
                            state = dismissState,
                            background = {},
                            dismissContent = {
                                FavouriteQuestionItem(item)
                            }
                        )
                    }
                }
            }
        }

        val itemToDelete = confirmDeleteItem.value
        if (itemToDelete != null) {
            AlertDialog(
                onDismissRequest = { confirmDeleteItem.value = null },
                title = { Text(text = "Delete Item") },
                text = { Text(text = "Are you sure you want to delete this item?") },
                confirmButton = {
                    Button(
                        onClick = {
                            questionViewModel.favoriteQuestion(itemToDelete, false)
                            confirmDeleteItem.value = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = "Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { confirmDeleteItem.value = null }) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun FavouriteQuestionItem(item: Question) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
    ) {
        Column(Modifier.padding(10.dp)) {
            Text(text = item.owner.display_name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = item.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 13.sp)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(item.tags) { tag ->
                    Text(
                        text = "#$tag",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(text = "${item.view_count} views")
                Text(text = "Answered: ${item.is_answered}")
                Text(text = "${item.answer_count} answers")
            }
        }
    }
}
