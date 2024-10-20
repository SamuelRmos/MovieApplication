package com.example.movie.ui.state

data class DetailsState(
    val director: String = "",
    val isLoading: Boolean = true,
    val error: String = ""
)