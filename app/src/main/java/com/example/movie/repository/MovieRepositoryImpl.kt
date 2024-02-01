package com.example.movie.repository

import com.example.commons.functional.Either
import com.example.movie.model.MovieResponse
import com.example.movie.network.MovieApi
import com.example.movie.persistence.MovieDao
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : BaseRepository(), MovieRepository {

    override suspend fun getListPopularMovies(): Either<String, MovieResponse> {
        return safeApiCall { movieApi.getPopularMovieAsync().await() }
    }

    override suspend fun getListRatedMovies(): Either<String, MovieResponse> {
        return safeApiCall { movieApi.getRatedMovieAsync().await() }
    }

    override suspend fun getListTodayMovies(): Either<String, MovieResponse> {
        return safeApiCall { movieApi.getTodayMovieAsync().await() }
    }

    override suspend fun getListClassicMovies(): Either<String, MovieResponse> {
        return safeApiCall { movieApi.getClassicMovieAsync().await() }
    }

    override fun getMoviePoster() = movieDao.getMovieList()
}