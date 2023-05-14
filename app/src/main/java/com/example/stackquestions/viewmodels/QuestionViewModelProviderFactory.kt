package com.example.stackquestions.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stackquestions.data.QuestionRepository

class QuestionViewModelProviderFactory(private val questionRepository: QuestionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionViewModel(questionRepository) as T
    }
}