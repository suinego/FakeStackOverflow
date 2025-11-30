package com.example.fakestackoverflow.data.repository

import com.example.fakestackoverflow.data.remote.AnswerModel
import com.example.fakestackoverflow.data.remote.QuestionModel
import com.example.fakestackoverflow.data.remote.StackApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkRepository(private val api: StackApi) {

    suspend fun getQuestionsFromNetwork(): Result<List<QuestionModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = api.getQuestions()
                Result.success(resp.items)
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
    }

    suspend fun getAnswersFromNetwork(questionId: Long): Result<List<AnswerModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = api.getAnswers(questionId)
                Result.success(resp.items)
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
    }
}
