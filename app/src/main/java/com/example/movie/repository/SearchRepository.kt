package com.example.movie.repository

import com.example.movie.model.SearchMovieItem
import com.example.movie.ui.details.DetailRequestState
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchMovies(query: String): Flow<DetailRequestState<List<SearchMovieItem>>>
}
