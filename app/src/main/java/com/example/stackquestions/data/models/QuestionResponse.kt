package com.example.stackquestions.data.models

data class QuestionResponse(
    val has_more: Boolean,
    val items: List<Question>,
    val quota_max: Int,
    val quota_remaining: Int
)