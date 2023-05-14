package com.example.stackquestions

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.stackquestions.data.QuestionDatabase
import com.example.stackquestions.data.QuestionRepository
import com.example.stackquestions.presentations.questionScreen.MainScreen
import com.example.stackquestions.ui.theme.StackQuestionsTheme
import com.example.stackquestions.presentations.questionScreen.viewmodels.QuestionViewModel
import com.example.stackquestions.presentations.questionScreen.viewmodels.QuestionViewModelProviderFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val questionRepository = QuestionRepository(QuestionDatabase(this))
            val questionViewModelProviderFactory = QuestionViewModelProviderFactory(questionRepository)
            val questionViewModel = ViewModelProvider(this,questionViewModelProviderFactory)[QuestionViewModel::class.java]
            StackQuestionsTheme {
                MainScreen(questionViewModel)
            }
        }
    }
}