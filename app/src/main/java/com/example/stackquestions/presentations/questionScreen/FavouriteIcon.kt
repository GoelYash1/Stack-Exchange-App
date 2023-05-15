package com.example.stackquestions.presentations.questionScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel

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