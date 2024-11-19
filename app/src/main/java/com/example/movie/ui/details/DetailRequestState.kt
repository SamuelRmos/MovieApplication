package com.example.movie.ui.details

import com.example.movie.model.MovieCredits
import com.example.movie.model.VideosList

sealed class DetailRequestState {
    data object Loading : DetailRequestState()
    data class Success(
        val credits: MovieCredits? = null,
        val videos: VideosList? = null
    ) : DetailRequestState()
    data class Error(val message: String) : DetailRequestState()
}