package com.example.movie.repository

import androidx.paging.PagingData
import com.example.movie.model.Movie
import com.example.movie.ui.RequestState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getListPopularMovies(): Flow<PagingData<Movie>>

    fun getListRatedMovies(): Flow<PagingData<Movie>>

    fun getListTodayMovies(): Flow<PagingData<Movie>>

    fun getClassicMoviesPaging(): Flow<PagingData<Movie>>
}