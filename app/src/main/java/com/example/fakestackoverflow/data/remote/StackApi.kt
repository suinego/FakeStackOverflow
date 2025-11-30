package com.example.fakestackoverflow.data.remote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface StackApi   {
    @GET("questions")
    suspend fun getQuestions(
        @Query("pagesize") pageSize: Int = 100,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "votes",
        @Query("tagged") tagged: String = "android",
        @Query("site") site: String = "stackoverflow",
        @Query("filter") filter: String = "withbody"
    ): ApiResponse<QuestionModel>
    @GET("questions/{id}/answers")
    suspend fun getAnswers(
        @Path("id") questionId: Long,
        @Query("pagesize") pageSize: Int = 100,
        @Query("order") order: String = "desc",
        @Query("sort") sort: String = "votes",
        @Query("site") site: String = "stackoverflow",
        @Query("filter") filter: String = "withbody"
    ): ApiResponse<AnswerModel>
}