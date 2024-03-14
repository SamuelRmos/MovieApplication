package com.example.movie.repository

import com.example.movie.ui.RequestState
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getListPopularMovies(): Flow<RequestState>

    suspend fun getListRatedMovies(): Flow<RequestState>

    suspend fun getListTodayMovies(): Flow<RequestState>

    suspend fun getListClassicMovies(): Flow<RequestState>
}