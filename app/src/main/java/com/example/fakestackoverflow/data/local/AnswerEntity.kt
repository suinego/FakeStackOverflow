package com.example.fakestackoverflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answers")
data class AnswerEntity(
    @PrimaryKey val answerId: Long,
    val questionId: Long,
    val body: String?,
    val score: Int?,
    val authorName: String?,
    val authorAvatar: String?,
    val isAccepted: Boolean = false
)
