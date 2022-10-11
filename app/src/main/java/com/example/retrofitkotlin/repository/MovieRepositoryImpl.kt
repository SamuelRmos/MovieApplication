package com.example.retrofitkotlin.repository

import com.example.commons.functional.Either
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.network.MovieApi
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.commons.service.ConnectionService
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao,
    connectionService: ConnectionService
) : BaseRepository(connectionService), MovieRepository {

    override suspend fun getListPopularMovies(): Either<String, MovieResponse> {
        return safeApiCall(call = { movieApi.getPopularMovieAsync().await() })
    }

    override suspend fun getListRatedMovies(): Either<String, MovieResponse> {
        return safeApiCall(call = { movieApi.getRatedMovieAsync().await() })
    }

    override suspend fun getListTodayMovies(): Either<String, MovieResponse> {
        return safeApiCall(call = { movieApi.getTodayMovieAsync().await() })
    }

    override suspend fun getListClassicMovies(): Either<String, MovieResponse> {
        return safeApiCall(call = { movieApi.getClassicMovieAsync().await() })
    }

    override fun getMoviePoster() = movieDao.getMovieList()

}