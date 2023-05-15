package com.example.stackquestions.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.stackquestions.data.models.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM Questions ORDER BY creation_date DESC")
    fun getAllQuestions(): Flow<List<Question>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)
    @Update
    suspend fun updateQuestion(question: Question)
    @Query("DELETE FROM Questions WHERE is_favourite IS NULL OR is_favourite = 0")
    suspend fun deleteNonFavouriteQuestions()
    @Query("SELECT * FROM Questions WHERE is_favourite = 1 ORDER BY creation_date DESC")
    fun getFavoriteQuestions(): List<Question>
}