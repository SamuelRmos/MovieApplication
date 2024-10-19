package com.example.movie.repository

import com.example.movie.network.MovieApi
import com.example.movie.ui.RequestState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : BaseRepository(), MovieRepository {
    override suspend fun getListPopularMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getPopularMovieAsync(page) }

    override suspend fun getListRatedMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getRatedMovieAsync(page) }

    override suspend fun getListTodayMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getTodayMovieAsync(page) }

    override suspend fun getListClassicMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getClassicMovieAsync(page) }
}