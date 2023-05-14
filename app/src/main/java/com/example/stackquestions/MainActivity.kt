package com.example.stackquestions

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.stackquestions.data.QuestionDatabase
import com.example.stackquestions.data.QuestionRepository
import com.example.stackquestions.ui.theme.StackQuestionsTheme
import com.example.stackquestions.viewmodels.QuestionViewModel
import com.example.stackquestions.viewmodels.QuestionViewModelProviderFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val questionRepository = QuestionRepository(QuestionDatabase(this))
            val viewModelProviderFactory = QuestionViewModelProviderFactory(questionRepository)
            val viewModel = ViewModelProvider(this,viewModelProviderFactory)[QuestionViewModel::class.java]
            StackQuestionsTheme {
                MainScreen(viewModel)
            }
        }
    }
}