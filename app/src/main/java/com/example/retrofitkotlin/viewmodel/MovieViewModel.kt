package com.example.retrofitkotlin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieUiState(
    val movie: List<Movie> = listOf(),
    val loading: Boolean = false
)

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {

    var uiState by mutableStateOf(MovieUiState(loading = true))
        private set

    init {
        getMoviesData()
    }

    private fun getMoviesData() {
        fetchRatedMovies()
        // fetchPopularMovies()
        //fetchTodayMovies()
        //fetchClassicMovies()
    }

    private fun fetchPopularMovies() {
        // _actionView.value = Loading(true)
        //  mUiScope.launch { executePopularMovie() }
    }

    private fun fetchRatedMovies() {
        viewModelScope.launch { executeRatedMovie() }
    }

    private fun fetchTodayMovies() {
        //_actionView.value = Loading(true)
        // mUiScope.launch {
        //      executeTodayMovie()
        // }
    }

    private fun fetchClassicMovies() {
        // _actionView.value = Loading(true)
        //  mUiScope.launch {
        //      executeClassicMovie()
        //   }
    }

    private suspend fun executePopularMovie() {
        viewModelScope.async {
            return@async movieUseCase.executePopularMovies()
        }.await().fold(::showError, ::updateUiState)
    }

    private suspend fun executeRatedMovie() {
        viewModelScope.async {
            return@async movieUseCase.executeRatedMovies()
        }.await().fold(::showError, ::updateUiState)
    }

    private suspend fun executeTodayMovie() {
        //   mIoScope.async {
        //        return@async movieUseCase.executeTodayMovies()
        //    }.await().fold(::showError, ::updateUiState)
    }

    private suspend fun executeClassicMovie() {
        //    mIoScope.async {
        //        return@async movieUseCase.executeClassicMovies()
        //    }.await().fold(::showError, ::updateUiState)
    }

    private fun showError(failure: String) {
        //  _actionView.value = Loading(false)
        //  _actionView.value = Error(failure)
    }

    private fun updateUiState(value: MovieResponse) {
        uiState = uiState.copy(loading = false, movie = value.results)
    }
}