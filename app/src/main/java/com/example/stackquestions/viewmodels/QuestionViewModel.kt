package com.example.stackquestions.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stackquestions.models.QuestionResponse
import com.example.stackquestions.repo.QuestionRepository
import com.example.stackquestions.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class QuestionViewModel(
    private val questionRepository: QuestionRepository
):ViewModel() {
    val questions: MutableLiveData<Resource<QuestionResponse>> = MutableLiveData()
    var questionPage = 1

    init {
        getQuestions()
    }
    private fun getQuestions() = viewModelScope.launch{
        questions.postValue(Resource.Loading())
        val response = questionRepository.getAllQuestions(questionPage)
        questions.postValue(handlingQuestionResponse(response))
    }
    private fun handlingQuestionResponse(response : Response<QuestionResponse>):Resource<QuestionResponse>{
        if (response.isSuccessful)
        {
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}