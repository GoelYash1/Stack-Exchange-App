package com.example.stackquestions.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stackquestions.models.Question

@Dao
interface QuestionDao {
    @Query("SELECT * FROM Questions")
    fun getAllQuestions(): LiveData<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: Question): Long

    @Query("DELETE FROM Questions")
    suspend fun deleteQuestion()
}