package com.example.movie.ui.details

sealed class DetailRequestState {
    object Loading : DetailRequestState()

    data class Success(val director: String) : DetailRequestState()

    data class Error(val message: String) : DetailRequestState()

    fun getDirectorName() = (this as Success).director
}