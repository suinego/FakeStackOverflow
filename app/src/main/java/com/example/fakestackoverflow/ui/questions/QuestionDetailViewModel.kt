package com.example.fakestackoverflow.ui.questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestackoverflow.data.RepositoryProvider
import com.example.fakestackoverflow.data.repository.Resource
import com.example.fakestackoverflow.model.AnswerUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DetailState {
    object Loading: DetailState()
    data class Success(val answers: List<com.example.fakestackoverflow.data.remote.AnswerModel>): DetailState()
    data class Error(val message: String): DetailState()
}

class QuestionDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RepositoryProvider.provideRepository(application.applicationContext)

    private val _answersState = MutableStateFlow<Resource<List<AnswerUiModel>>>(Resource.Loading())
    val answersState: StateFlow<Resource<List<AnswerUiModel>>> = _answersState

    fun loadAnswers(questionId: Long) {
        viewModelScope.launch {
            _answersState.value = Resource.Loading()
            try {
                val res = repo.getAnswersForQuestion(questionId)
                if (res.isSuccess) {
                    val models = res.getOrNull() ?: emptyList()
                    val ui = models.map { m ->
                        AnswerUiModel(
                            answerId = m.answerId,
                            body = m.body,
                            score = m.score ?: 0,
                            authorName = m.owner?.displayName,
                            authorAvatar = m.owner?.profileImage,
                            isAccepted = m.isAccepted ?: false
                        )
                    }
                    _answersState.value = Resource.Success(ui)
                } else {
                    val ex = res.exceptionOrNull()
                    _answersState.value = Resource.Error(ex?.localizedMessage ?: "Unknown error")
                }
            } catch (t: Throwable) {
                _answersState.value = Resource.Error(t.localizedMessage ?: t.javaClass.simpleName)
            }
        }
    }
}
