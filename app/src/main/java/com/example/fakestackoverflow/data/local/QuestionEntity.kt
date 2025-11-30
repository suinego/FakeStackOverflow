package com.example.fakestackoverflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val questionId: Long,
    val title: String,
    val body: String?,
    val score: Int,
    val authorName: String?,
    val authorAvatar: String?,
    val lastActivityDate: Long?
)
