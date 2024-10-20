package com.example.movie.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Movie
import com.example.movie.repository.MovieRepository
import com.example.movie.ui.RequestState.Error
import com.example.movie.ui.RequestState.Loading
import com.example.movie.ui.RequestState.Success
import com.example.movie.ui.state.MovieState
import com.example.movie.ui.state.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _statePopularMovie = MutableStateFlow(MovieState())
    val statePopularMovie = _statePopularMovie.asStateFlow()

    private val _paginationPopularState = MutableStateFlow(PaginationState())
    val paginationPopularState = _paginationPopularState.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    init {
        fetchMovies()
    }

    @VisibleForTesting
   internal fun fetchMovies(page: Int = 1) {
        viewModelScope.launch(IO) {
            movieRepository.getListPopularMovies(page)
                .distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Success -> onRequestSuccess(result.data)
                        is Loading -> onRequestLoading()
                        is Error -> onRequestError(result.message)
                    }
                }
        }
    }

    @VisibleForTesting
    internal fun onRequestLoading() {
        if (_statePopularMovie.value.movies.isEmpty()) {
            _statePopularMovie.update {
                it.copy(isLoading = true)
            }
        }

        if (_statePopularMovie.value.movies.isNotEmpty()) {
            _paginationPopularState.update {
                it.copy(isLoading = true)
            }
        }
    }

    @VisibleForTesting
    internal fun onRequestSuccess(data: List<Movie>) {
        val movies = _statePopularMovie.value.movies + data
        _statePopularMovie.update {
            it.copy(movies = movies, isLoading = false)
        }

        val listSize = _statePopularMovie.value.movies.size
        _paginationPopularState.update {
            it.copy(
                skip = it.skip + 1,
                endReached = data.isEmpty() || listSize >= MOVIES_LIMIT,
                isLoading = false
            )
        }
    }

    @VisibleForTesting
    internal fun onRequestError(message: String?) {
        _statePopularMovie.update {
            it.copy(error = message ?: "Unexpected Error", isLoading = false)
        }
    }

    fun getMoviesPaginated() {
        if (_statePopularMovie.value.movies.isEmpty()) return

        if (_paginationPopularState.value.endReached) return

        fetchMovies(_paginationPopularState.value.skip)
    }

    fun refresh() {
        viewModelScope.launch(IO) {
            updateRefreshState(true)
            _paginationPopularState.update { it.copy(skip = 0) }
            _statePopularMovie.update { it.copy(movies = _statePopularMovie.value.movies) }
            fetchMovies()
            updateRefreshState(false)
        }
    }

    @VisibleForTesting
    internal fun updateRefreshState(value: Boolean) = _isRefresh.update { value }
}