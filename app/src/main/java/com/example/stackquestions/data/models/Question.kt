package com.example.stackquestions.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.stackquestions.util.DataConverter

@Entity(tableName = "Questions")
data class Question(
    val link: String,
    val question_id: Int,
    val accepted_answer_id: Int?,
    val answer_count: Int,
    val bounty_amount: Int?,
    val bounty_closes_date: Int?,
    val content_license: String?,
    val creation_date: Int,
    val is_answered: Boolean,
    val last_activity_date: Int,
    val last_edit_date: Int?,
    val owner: Owner,
    val score: Int,
    val tags: List<String>,
    @PrimaryKey val title: String,
    val view_count: Int,
    var is_favourite: Boolean?
)
