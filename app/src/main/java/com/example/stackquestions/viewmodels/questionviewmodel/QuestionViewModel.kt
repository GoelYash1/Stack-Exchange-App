package com.example.stackquestions.viewmodels.questionviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.stackquestions.data.QuestionRepository
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(
    private val questionRepository: QuestionRepository
) : ViewModel() {
    private val _questions = MediatorLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>> = _questions

    private val _favoriteQuestions = MutableLiveData<List<Question>>()
    val favoriteQuestions: LiveData<List<Question>> = _favoriteQuestions

    private val _refreshing = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> = _refreshing

    init {
        _questions.addSource(questionRepository.getQuestions().asLiveData()) { result ->
            _questions.value = result
        }
        loadFavoriteQuestions()
    }

    private fun loadFavoriteQuestions() {
        viewModelScope.launch {
            val favoriteQuestions = withContext(Dispatchers.IO) {
                questionRepository.getFavoriteQuestions()
            }
            _favoriteQuestions.value = favoriteQuestions
        }
    }

    fun favoriteQuestion(question: Question, isFavorite: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                question.is_favourite = isFavorite
                questionRepository.updateQuestion(question)
            }

            if (isFavorite) {
                _favoriteQuestions.value = (_favoriteQuestions.value ?: emptyList()) + question
            } else {
                _favoriteQuestions.value =
                    (_favoriteQuestions.value ?: emptyList()).filter { it.question_id != question.question_id }
            }
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

