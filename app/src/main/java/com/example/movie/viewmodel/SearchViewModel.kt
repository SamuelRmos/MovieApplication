package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.repository.SearchRepository
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import com.example.movie.ui.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DEFAULT_DEBOUNCE_MS = 350L

@HiltViewModel
class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val dispatcher: CoroutineDispatcher,
    private val debounceMs: Long = DEFAULT_DEBOUNCE_MS
) : ViewModel() {

    @Inject
    constructor(searchRepository: SearchRepository) : this(
        searchRepository = searchRepository,
        dispatcher = Dispatchers.IO,
        debounceMs = DEFAULT_DEBOUNCE_MS
    )

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private var searchJob: Job? = null
    private var lastSearchedQuery: String = ""

    fun onQueryChange(newQuery: String) {
        _searchState.update { it.copy(query = newQuery) }
        val trimmedQuery = newQuery.trim()
        if (trimmedQuery.isEmpty()) {
            searchJob?.cancel()
            lastSearchedQuery = ""
            _searchState.update {
                it.copy(
                    results = emptyList(),
                    isLoading = false,
                    error = ""
                )
            }
            return
        }

        if (trimmedQuery == lastSearchedQuery && _searchState.value.error.isEmpty()) {
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch(dispatcher) {
            if (debounceMs > 0L) {
                delay(debounceMs)
            }
            lastSearchedQuery = trimmedQuery
            searchRepository.searchMovies(trimmedQuery)
                .distinctUntilChanged()
                .collectLatest { state ->
                    when (state) {
                        is Loading -> _searchState.update {
                            it.copy(isLoading = true, error = "")
                        }
                        is Success -> _searchState.update {
                            it.copy(
                                results = state.data,
                                isLoading = false,
                                error = ""
                            )
                        }
                        is Error -> _searchState.update {
                            it.copy(
                                results = emptyList(),
                                isLoading = false,
                                error = state.message
                            )
                        }
                    }
                }
        }
    }
}
