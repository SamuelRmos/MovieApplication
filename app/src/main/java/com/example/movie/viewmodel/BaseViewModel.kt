package com.example.movie.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commons.functional.Either
import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse
import kotlinx.coroutines.async

data class MovieUiState(val moviesList: List<Movie>, val loading: Boolean)

open class BaseViewModel : ViewModel() {

    var uiState by mutableStateOf(MovieUiState(listOf(), true))
        private set

    suspend fun executeCall(call: Either<String, MovieResponse>) {
        viewModelScope.async {
            return@async call
        }.await().fold(::showError, ::updateUiState)
    }

    private fun showError(failure: String) {
       //  _actionView.value = Loading(false)
        //  _actionView.value = Error(failure)
    }

    private fun updateUiState(response: MovieResponse) {
        uiState = uiState.copy(loading = false, moviesList = response.results)
    }
}