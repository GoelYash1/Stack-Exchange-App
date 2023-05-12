package com.example.stackquestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stackquestions.repo.QuestionRepository
import com.example.stackquestions.viewmodels.QuestionViewModel

class QuestionViewModelProviderFactory(
    val questionRepository: QuestionRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuestionViewModel(questionRepository) as T
    }
}