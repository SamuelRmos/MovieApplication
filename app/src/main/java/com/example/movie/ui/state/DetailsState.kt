package com.example.movie.ui.state

import com.example.movie.model.Cast

data class DetailsState(
    val director: String = "",
    val cast: List<Cast> = emptyList(),
    val isLoading: Boolean = true,
    val error: String = "",
    val key: String = ""
)
