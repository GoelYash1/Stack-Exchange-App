package com.example.stackquestions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stackquestions.db.QuestionDatabase
import com.example.stackquestions.repo.QuestionRepository
import com.example.stackquestions.ui.theme.StackQuestionsTheme
import com.example.stackquestions.viewmodels.QuestionViewModel

class MainActivity : ComponentActivity() {
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