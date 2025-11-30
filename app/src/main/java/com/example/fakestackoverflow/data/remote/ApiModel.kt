package com.example.fakestackoverflow.data.remote

import com.squareup.moshi.Json

data class ApiResponse<T>(
    val items: List<T> = emptyList(),
    @Json(name = "has_more") val hasMore: Boolean = false,
    @Json(name = "quota_max") val quotaMax: Int? = null,
    @Json(name = "quota_remaining") val quotaRemaining: Int? = null
)

data class QuestionModel(
    @Json(name = "question_id") val questionId: Long,
    val title: String?,
    val body: String?,
    val score: Int?,
    val owner: OwnerModel?,
    @Json(name = "last_activity_date") val lastActivityDate: Long?
)

data class OwnerModel(
    @Json(name = "display_name") val displayName: String?,
    @Json(name = "profile_image") val profileImage: String?
)

data class AnswerModel(
    @Json(name = "answer_id") val answerId: Long,
    val body: String?,
    val score: Int?,
    val owner: OwnerModel?,
    @Json(name = "is_accepted") val isAccepted: Boolean?
)
