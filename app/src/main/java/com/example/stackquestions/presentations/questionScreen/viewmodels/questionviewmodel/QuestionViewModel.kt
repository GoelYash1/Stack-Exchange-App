package com.example.stackquestions.presentations.questionScreen.viewmodels.questionviewmodel

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
    private val _questions = MediatorLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>> = _questions

    private val _refreshing = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> = _refreshing

    init {
        _questions.addSource(questionRepository.getQuestions().asLiveData()) { result ->
            _questions.value = result
        }
    }

    fun favoriteQuestion(question: Question, isFavorite: Boolean) {
        viewModelScope.launch {
            question.is_favourite = isFavorite
            questionRepository.updateQuestion(question)
        }
    }

    fun updateQuestions() {
        _refreshing.value = true
        _questions.removeSource(questions)
        _questions.addSource(questionRepository.getQuestions().asLiveData()) { result ->
            _questions.value = result
            _refreshing.value = false
        }
    }
}
