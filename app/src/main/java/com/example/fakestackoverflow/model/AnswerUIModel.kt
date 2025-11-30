package com.example.fakestackoverflow.model

data class AnswerUiModel(
    val answerId: Long,
    val body: String?,
    val score: Int,
    val authorName: String?,
    val authorAvatar: String?,
    val isAccepted: Boolean = false
)
