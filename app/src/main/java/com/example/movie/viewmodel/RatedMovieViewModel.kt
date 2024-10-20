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
class RatedMovieViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private val _stateRatedMovie = MutableStateFlow(MovieState())
    val stateRatedMovie = _stateRatedMovie.asStateFlow()

    private val _paginationRatedState = MutableStateFlow(PaginationState())
    val paginationRatedState = _paginationRatedState.asStateFlow()

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh: StateFlow<Boolean> = _isRefresh

    init {
        fetchMovies()
    }

    @VisibleForTesting
    internal fun fetchMovies(page: Int = 1) {
        viewModelScope.launch(IO) {
            movieRepository.getListRatedMovies(page)
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
    internal fun onRequestSuccess(data: List<Movie>) {
        val movies = _stateRatedMovie.value.movies + data
        _stateRatedMovie.update {
            it.copy(movies = movies, isLoading = false)
        }

        val listSize = _stateRatedMovie.value.movies.size
        _paginationRatedState.update {
            it.copy(
                skip = it.skip + 1,
                endReached = data.isEmpty() || listSize >= MOVIES_LIMIT,
                isLoading = false
            )
        }
    }

    @VisibleForTesting
    internal fun onRequestLoading() {
        if (_stateRatedMovie.value.movies.isEmpty()) {
            _stateRatedMovie.update {
                it.copy(isLoading = true)
            }
        }

        if (_stateRatedMovie.value.movies.isNotEmpty()) {
            _paginationRatedState.update {
                it.copy(isLoading = true)
            }
        }
    }

    @VisibleForTesting
    internal fun onRequestError(message: String?) {
        _stateRatedMovie.update {
            it.copy(error = message ?: "Unexpected Error", isLoading = false)
        }
    }

    fun getMoviesPaginated() {
        if (_stateRatedMovie.value.movies.isEmpty()) return

        if (_paginationRatedState.value.endReached) return

        fetchMovies(_paginationRatedState.value.skip)
    }

    fun refresh() {
        viewModelScope.launch(IO) {
            updateRefreshState(true)
            _paginationRatedState.update { it.copy(skip = 0) }
            _stateRatedMovie.update { it.copy(movies = _stateRatedMovie.value.movies) }
            fetchMovies()
            updateRefreshState(false)
        }
    }

    @VisibleForTesting
    internal fun updateRefreshState(value: Boolean) = _isRefresh.update { value }
}