package com.example.stackquestions.presentations.questionScreen.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.stackquestions.data.QuestionRepository
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.Resource
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val questionRepository: QuestionRepository
): ViewModel() {
    private var currentPage = 1
    private val pageSize = 30

    private val _questions = MediatorLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>> = _questions

    private val _refreshing = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> = _refreshing

    init {
        getQuestions()
    }

    fun favoriteQuestion(question: Question, isFavorite: Boolean) {
        viewModelScope.launch {
            question.is_favourite = isFavorite
            questionRepository.updateQuestion(question)
        }
    }

    fun updateQuestions(isSwipeRefresh: Boolean = false) {
        if (isSwipeRefresh) {
            currentPage = 1
        } else {
            currentPage++
        }
        getQuestions()
    }


    private fun getQuestions() {
        _questions.addSource(questionRepository.getQuestions(currentPage, pageSize).asLiveData()) { result ->
            _questions.value = result
        }
    }
}
