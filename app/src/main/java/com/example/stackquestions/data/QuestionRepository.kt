package com.example.stackquestions.data

import androidx.lifecycle.ViewModel
import androidx.room.withTransaction
import com.example.stackquestions.api.StackExchangeClient
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.Resource
import com.example.stackquestions.util.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuestionRepository(
    private val db: QuestionDatabase
):ViewModel() {
    private val questionDao = db.questionDao()
    fun getQuestions() = networkBoundResource(
        query = {
            questionDao.getAllQuestions()
        },
        fetch = {
            delay(2000)
            StackExchangeClient.api.getQuestionDetails()
        },
        saveFetchResult = { questions ->
            db.withTransaction {
                questionDao.deleteQuestions()
                questionDao.insertQuestions(questions.items)
            }
        }
    )
    fun getSearchResults(queryText: String,pageNumber: Int): Flow<Resource<List<Question>>> = flow {
        emit(Resource.Loading())
        try {
            val searchResults = StackExchangeClient.api.getFilteredQuestions(queryText).items
            emit(Resource.Success(searchResults))
        } catch (throwable: Throwable) {
            emit(Resource.Error(throwable))
        }
    }
    suspend fun updateQuestion(question: Question) {
        questionDao.updateQuestion(question)
    }
}