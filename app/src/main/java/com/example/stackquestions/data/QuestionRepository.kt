package com.example.stackquestions.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.withTransaction
import com.example.stackquestions.api.StackExchangeClient
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.Resource
import com.example.stackquestions.util.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class QuestionRepository(
    private val db: QuestionDatabase
):ViewModel() {
    private val questionDao = db.questionDao()
    fun getQuestions(pageNumber: Int) = networkBoundResource(
        query = {
            questionDao.getAllQuestions()
        },
        fetch = {
            delay(2000)
            StackExchangeClient.api.getQuestionDetails(pageNumber = pageNumber)
        },
        saveFetchResult = { questions ->
            db.withTransaction {
                val existingFavourites = questionDao.getAllQuestions().firstOrNull()?.filter { it.is_favourite == true } ?: emptyList()
                val newQuestions = questions.items.filter { newQuestion ->
                    val existingQuestion = existingFavourites.firstOrNull { it.question_id == newQuestion.question_id }
                    if (existingQuestion != null) {
                        questionDao.updateQuestion(newQuestion.copy(is_favourite = true))
                        false
                    } else {
                        true
                    }
                }
                questionDao.deleteNonFavouriteQuestions()
                questionDao.insertQuestions(newQuestions)
            }
        }
    )
    fun getSearchResults(queryText: String,pageNumber: Int): Flow<Resource<List<Question>>> = flow {
        emit(Resource.Loading())
        try {
            val searchResults = StackExchangeClient.api.getFilteredQuestions(queryText,pageNumber).items
            emit(Resource.Success(searchResults))
        } catch (throwable: Throwable) {
            emit(Resource.Error(throwable))
        }
    }
    suspend fun updateQuestion(question: Question) {
        questionDao.updateQuestion(question)
    }
}