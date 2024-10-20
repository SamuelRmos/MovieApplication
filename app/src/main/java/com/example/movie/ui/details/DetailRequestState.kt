package com.example.movie.ui.details

import com.example.movie.model.MovieCredits

sealed class DetailRequestState {
    data object Loading : DetailRequestState()
    data class Success(val credits: MovieCredits) : DetailRequestState()
    data class Error(val message: String) : DetailRequestState()
}