package com.example.fakestackoverflow.data.repository

import android.content.Context
import android.util.Log
import com.example.fakestackoverflow.data.toEntity
import com.example.fakestackoverflow.data.remote.OwnerModel
import com.example.fakestackoverflow.data.toUiModel
import com.example.fakestackoverflow.data.local.AnswerDao
import com.example.fakestackoverflow.data.local.AnswerEntity
import com.example.fakestackoverflow.data.local.QuestionDao
import com.example.fakestackoverflow.data.local.QuestionEntity
import com.example.fakestackoverflow.data.remote.AnswerModel
import com.example.fakestackoverflow.data.remote.StackApi
import com.example.fakestackoverflow.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

sealed class Resource<T> {
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String) : Resource<T>()
}

class StackRepository(
    private val context: Context,
    private val api: StackApi,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao
) {

    fun getQuestionsFlow(): Flow<Resource<List<QuestionEntity>>> = flow {
        emit(Resource.Loading())

        val online = NetworkUtils.isOnline(context)
        if (online) {
            try {
                val resp = api.getQuestions()
                val items = resp.items.map { it.toEntity() }
                // save to DB
                questionDao.insertAll(items)
                val cached = questionDao.getAll()
                emit(Resource.Success(cached))
            } catch (t: Throwable) {
                val cached = questionDao.getAll()
                if (cached.isNotEmpty()) emit(Resource.Success(cached))
                else emit(Resource.Error("Ошибка сети: ${t.localizedMessage ?: t.javaClass.simpleName}"))
            }
        } else {
            val cached = questionDao.getAll()
            if (cached.isNotEmpty()) emit(Resource.Success(cached))
            else emit(Resource.Error("Нет интернета и нет кешированных данных"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getAnswersForQuestion(questionId: Long): Result<List<AnswerModel>> {
        return try {
            if (NetworkUtils.isOnline(context)) {
                val resp = api.getAnswers(questionId)
                val items: List<AnswerModel> = resp.items

                val entities: List<com.example.fakestackoverflow.data.local.AnswerEntity> =
                    items.map { dto ->
                        dto.toEntity(questionId)
                    }

                answerDao.deleteByQuestion(questionId)
                answerDao.insertAll(entities)

                Result.success(items)
            } else {
                // оффлайн: читаем из БД
                val cached: List<com.example.fakestackoverflow.data.local.AnswerEntity> =
                    answerDao.getByQuestion(questionId)
                if (cached.isNotEmpty()) {
                    val asModels = cached.map { e ->
                        AnswerModel(
                            answerId = e.answerId,
                            body = e.body,
                            score = e.score,
                            owner = com.example.fakestackoverflow.data.remote.OwnerModel(
                                displayName = e.authorName,
                                profileImage = e.authorAvatar
                            ),
                            isAccepted = e.isAccepted
                        )
                    }
                    Result.success(asModels)
                } else {
                    Result.failure(Exception("нет кеша"))
                }
            }
        } catch (t: Throwable) {
            // fallback to cached if available
            val cached = answerDao.getByQuestion(questionId)
            if (cached.isNotEmpty()) {
                val asModels = cached.map { e ->
                    AnswerModel(
                        answerId = e.answerId,
                        body = e.body,
                        score = e.score,
                        owner = com.example.fakestackoverflow.data.remote.OwnerModel(
                            displayName = e.authorName,
                            profileImage = e.authorAvatar
                        ),
                        isAccepted = e.isAccepted
                    )
                }
                Result.success(asModels)
            } else {
                Result.failure(t)
            }
        }
    }

}
