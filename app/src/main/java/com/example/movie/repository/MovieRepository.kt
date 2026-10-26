package com.example.movie.repository

import androidx.paging.PagingData
import com.example.movie.model.Movie
import com.example.movie.ui.RequestState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getListPopularMovies(page: Int): Flow<RequestState>

    suspend fun getListRatedMovies(page: Int): Flow<RequestState>

    suspend fun getListTodayMovies(page: Int): Flow<RequestState>

    suspend fun getListClassicMovies(page: Int): Flow<RequestState>

    fun getClassicMoviesPaging(): Flow<PagingData<Movie>>
}