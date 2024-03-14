package com.example.movie.repository

import com.example.movie.network.MovieApi
import com.example.movie.ui.RequestState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : BaseRepository(), MovieRepository {
    override suspend fun getListPopularMovies(): Flow<RequestState> =
        safeApiCall { movieApi.getPopularMovieAsync() }

    override suspend fun getListRatedMovies(): Flow<RequestState> =
        safeApiCall { movieApi.getRatedMovieAsync() }

    override suspend fun getListTodayMovies(): Flow<RequestState> =
        safeApiCall { movieApi.getTodayMovieAsync() }

    override suspend fun getListClassicMovies(): Flow<RequestState> =
        safeApiCall { movieApi.getClassicMovieAsync() }
}