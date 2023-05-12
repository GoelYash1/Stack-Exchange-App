package com.example.stackquestions.repo

import androidx.lifecycle.ViewModel
import com.example.stackquestions.api.StackExchangeClient
import com.example.stackquestions.db.QuestionDatabase
import retrofit2.Retrofit

class QuestionRepository(
    val db: QuestionDatabase
):ViewModel() {
    suspend fun getAllQuestions(pageNumber:Int) =
        StackExchangeClient.api.getQuestionDetails(pageNumber = pageNumber)
}