package com.example.retrofitkotlin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commons.functional.Either
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.model.MovieResponse
import kotlinx.coroutines.async

data class MovieUiState(
    val movie: List<Movie> = listOf(),
    val loading: Boolean = false
)

open class BaseViewModel : ViewModel() {

    var uiState by mutableStateOf(MovieUiState(loading = true))
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

    private fun updateUiState(value: MovieResponse) {
        uiState = uiState.copy(loading = false, movie = value.results)
    }
}