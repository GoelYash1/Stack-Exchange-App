package com.example.stackquestions.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.DataConverter

@Database(entities = [Question::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    companion object{
        @Volatile
        private var instance: QuestionDatabase?=null
        private val LOCK =Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance = it}
        }
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context = context.applicationContext,
                QuestionDatabase::class.java,
                "question_db.db"
            ).build()
    }
}