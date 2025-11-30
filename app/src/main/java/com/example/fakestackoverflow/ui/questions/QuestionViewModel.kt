package com.example.fakestackoverflow.ui.questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestackoverflow.data.RepositoryProvider
import com.example.fakestackoverflow.data.repository.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RepositoryProvider.provideRepository(application.applicationContext)

    private val _state = MutableStateFlow<Resource<List<com.example.fakestackoverflow.data.local.QuestionEntity>>>(Resource.Loading())
    val state: StateFlow<Resource<List<com.example.fakestackoverflow.data.local.QuestionEntity>>> = _state

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            repo.getQuestionsFlow().collectLatest { res ->
                _state.value = res
            }
        }
    }
}
