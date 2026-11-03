package com.example.movie.ui.details

sealed interface DetailRequestState<out T> {
    data object Loading : DetailRequestState<Nothing>
    data class Success<out T>(val data: T) : DetailRequestState<T>
    data class Error(val message: String) : DetailRequestState<Nothing>
}
