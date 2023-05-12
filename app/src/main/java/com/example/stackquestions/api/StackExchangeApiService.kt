package com.example.stackquestions.api

import com.example.stackquestions.models.QuestionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StackExchangeApiService{
    @GET("/questions")
    suspend fun getQuestionDetails(
        @Query("key") key: String,
        @Query("order") order: String,
        @Query("sort") sort: String,
        @Query("site") site: String
    ): QuestionResponse

    @GET("/search/advanced")
    suspend fun getFilteredQuestions(
        @Query("q") searchQuery:String
    ): QuestionResponse
}