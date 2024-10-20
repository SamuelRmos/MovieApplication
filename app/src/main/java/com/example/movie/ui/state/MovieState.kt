package com.example.movie.ui.state

import com.example.movie.model.Movie

data class MovieState(
    val isLoading: Boolean = true,
    val movies: List<Movie> = listOf(),
    val error: String = ""
)