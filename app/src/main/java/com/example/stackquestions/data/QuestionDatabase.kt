package com.example.stackquestions.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stackquestions.data.models.Question
import com.example.stackquestions.util.DataConverter

@Database(entities = [Question::class], version = 2)
@TypeConverters(DataConverter::class)
abstract class QuestionDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        @Volatile
        private var instance: QuestionDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                QuestionDatabase::class.java,
                "question_db.db"
            ).addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE Questions_new (title TEXT PRIMARY KEY NOT NULL, accepted_answer_id INTEGER, answer_count INTEGER NOT NULL, bounty_amount INTEGER, bounty_closes_date INTEGER, content_license TEXT, creation_date INTEGER NOT NULL, is_answered INTEGER NOT NULL, last_activity_date INTEGER NOT NULL, last_edit_date INTEGER, link TEXT NOT NULL, owner TEXT NOT NULL, score INTEGER NOT NULL, tags TEXT NOT NULL, question_id INTEGER NOT NULL, view_count INTEGER NOT NULL, is_favourite INTEGER)")
                    database.execSQL("INSERT INTO Questions_new (title, accepted_answer_id, answer_count, bounty_amount, bounty_closes_date, content_license, creation_date, is_answered, last_activity_date, last_edit_date, link, owner, score, tags, question_id, view_count) SELECT title, accepted_answer_id, answer_count, bounty_amount, bounty_closes_date, content_license, creation_date, is_answered, last_activity_date, last_edit_date, link, owner, score, tags, question_id, view_count FROM Questions")
                    database.execSQL("DROP TABLE Questions")
                    database.execSQL("ALTER TABLE Questions_new RENAME TO Questions")
                }
            }).build()
    }
}
