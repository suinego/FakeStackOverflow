package com.example.fakestackoverflow.data

import com.example.fakestackoverflow.data.local.QuestionEntity
import com.example.fakestackoverflow.data.local.AnswerEntity
import com.example.fakestackoverflow.data.remote.QuestionModel
import com.example.fakestackoverflow.data.remote.AnswerModel
import com.example.fakestackoverflow.model.AnswerUiModel
import com.example.fakestackoverflow.model.QuestionUIModel


fun QuestionModel.toEntity(): QuestionEntity {
    return QuestionEntity(
        questionId = this.questionId,
        title = this.title ?: "(no title)",
        body = this.body,
        score = this.score ?: 0,
        authorName = this.owner?.displayName,
        authorAvatar = this.owner?.profileImage,
        lastActivityDate = this.lastActivityDate
    )
}

fun QuestionEntity.toUiModel(): QuestionUIModel {
    return QuestionUIModel(
        questionId = this.questionId,
        title = this.title,
        body = this.body,
        score = this.score,
        author = this.authorName,
        authorAvatar = this.authorAvatar
    )
}

fun AnswerModel.toUiModel(): AnswerUiModel {
    return AnswerUiModel(
        answerId = this.answerId,
        body = this.body,
        score = this.score ?: 0,
        authorName = this.owner?.displayName,
        authorAvatar = this.owner?.profileImage,
        isAccepted = this.isAccepted ?: false
    )
}
fun AnswerModel.toEntity(questionId: Long): AnswerEntity {
    return AnswerEntity(
        answerId = this.answerId,
        questionId = questionId,
        body = this.body,
        score = this.score,
        authorName = this.owner?.displayName,
        authorAvatar = this.owner?.profileImage,
        isAccepted = this.isAccepted ?: false
    )
}
