package com.example.stackquestions.presentations.questionScreen

import android.os.Build
import android.webkit.WebView
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.helpers.HelperFunctions
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayQuestionItemUI(
    question: Question,
    viewModel: QuestionViewModel
) {
    var showWebView by remember { mutableStateOf(false) }
    val minutesAgo = HelperFunctions.getTimeAgo(question.creation_date.toLong())
    Box(
        modifier = Modifier
            .clickable(
                onClick = {showWebView = true}
            )
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
            if (showWebView) {
                WebViewComponent(url = question.link)
            }
        }
    )
}

@Composable
fun WebViewComponent(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // WebView settings can be configured here
                settings.javaScriptEnabled = true
            }
        },
        update = { webView ->
            webView.loadUrl(url)
        }
    )
}