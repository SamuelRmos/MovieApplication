package com.example.movie.ui.state

import com.example.movie.model.SearchMovieItem

data class SearchState(
    val query: String = "",
    val results: List<SearchMovieItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

