package com.example.stackquestions.api

import com.example.stackquestions.data.models.QuestionResponse
import com.example.stackquestions.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StackExchangeApiService{
    @GET("questions?pagesize=100&sort=creation&order=desc")
    suspend fun getQuestionDetails(
        @Query("apikey") key: String = API_KEY,
        @Query("site") site: String = "stackoverflow"
    ): QuestionResponse

    @GET("search/advanced?pagesize=100&order=activity&sort=creation")
    suspend fun getFilteredQuestions(
        @Query("q") searchQuery:String,
        @Query("apikey") key: String = API_KEY,
        @Query("site") site: String = "stackoverflow"
    ): QuestionResponse
}