package com.example.stackquestions.viewmodels.searchviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stackquestions.data.QuestionRepository
import com.example.stackquestions.viewmodels.questionviewmodel.QuestionViewModel

class SearchViewModelFactory(private val questionRepository: QuestionRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(questionRepository) as T
    }
}