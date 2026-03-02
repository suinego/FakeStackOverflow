package com.example.fakestackoverflow.ui.questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakestackoverflow.data.RepositoryProvider
import com.example.fakestackoverflow.data.repository.Resource
import com.example.fakestackoverflow.data.toUiModel
import com.example.fakestackoverflow.model.QuestionUIModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuestionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = RepositoryProvider.provideRepository(application.applicationContext)

    private val _allQuestions =
        MutableStateFlow<Resource<List<QuestionUIModel>>>(Resource.Loading())

    val searchQuery = MutableStateFlow("")
    val selectedTag = MutableStateFlow<String?>(null)

    val state: StateFlow<Resource<List<QuestionUIModel>>> = combine(
        _allQuestions, searchQuery, selectedTag
    ) { resource, query, tag ->
        when (resource) {
            is Resource.Loading -> Resource.Loading()
            is Resource.Error -> Resource.Error(resource.message)
            is Resource.Success -> {
                val filtered = resource.data
                    .let { list ->
                        if (query.isBlank()) list
                        else list.filter { it.title.contains(query, ignoreCase = true) }
                    }
                    .let { list ->
                        if (tag == null) list
                        else list.filter { tag in it.tags }
                    }
                Resource.Success(filtered)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    val availableTags: StateFlow<List<String>> = _allQuestions.map { res ->
        if (res is Resource.Success) res.data.flatMap { it.tags }.distinct().sorted()
        else emptyList()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        loadQuestions()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            repo.getQuestionsFlow().collectLatest { res ->
                _allQuestions.value = when (res) {
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Error -> Resource.Error(res.message)
                    is Resource.Success -> Resource.Success(res.data.map { it.toUiModel() })
                }
            }
        }
    }
}