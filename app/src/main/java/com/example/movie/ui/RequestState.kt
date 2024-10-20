package com.example.movie.ui

import com.example.movie.model.Movie

sealed class RequestState {
    data object Loading : RequestState()
    data class Success(val data: List<Movie>) : RequestState()
    data class Error(val message: String) : RequestState()
}