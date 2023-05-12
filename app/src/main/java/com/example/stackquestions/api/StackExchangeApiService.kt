package com.example.stackquestions.api

import com.example.stackquestions.models.QuestionResponse
import com.example.stackquestions.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StackExchangeApiService{
    @GET("questions")
    suspend fun getQuestionDetails(
        @Query("apikey") key: String = API_KEY,
        @Query("page") pageNumber: Int,
        @Query("site") site: String = "stackoverflow"
    ): Response<QuestionResponse>

    @GET("search/advanced")
    suspend fun getFilteredQuestions(
        @Query("q") searchQuery:String,
        @Query("page") pageNumber: Int,
        @Query("apikey") key: String = API_KEY,
        @Query("site") site: String = "stackoverflow"
    ): Response<QuestionResponse>
}