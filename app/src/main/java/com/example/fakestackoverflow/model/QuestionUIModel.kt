package com.example.fakestackoverflow.model

class QuestionUIModel(
    val questionId: Long,
    val title: String,
    val body: String?,
    val score: Int,
    val author: String?,
    val authorAvatar: String? = null
)