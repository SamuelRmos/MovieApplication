package com.example.movieapp.repository

import com.example.movieapp.functional.Either
import com.example.movieapp.model.MovieResponse
import com.example.movieapp.network.MovieApi
import com.example.movieapp.persistence.MovieDao
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : BaseRepository(), MovieRepository {

    override suspend fun getListPopularMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse> {
        return if (isConnected) {
            safeApiCall(
                call = { movieApi.getPopularMovieAsync().await() },
                errorMessage = "Error Fetching Popular Movies"
            )

        } else errorConnection()
    }

    override suspend fun getListRatedMovies(isConnected: Boolean)
            : Either<String, MovieResponse> {
        return if (isConnected) {
            safeApiCall(
                call = { movieApi.getRatedMovieAsync().await() },
                errorMessage = "Error Fetching Popular Movies"
            )

        } else errorConnection()
    }

    override suspend fun getListTodayMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse> {
        return if (isConnected) {
            safeApiCall(
                call = { movieApi.getTodayMovieAsync().await() },
                errorMessage = "Error Fetching Popular Movies"
            )

        } else errorConnection()
    }

    override suspend fun getListClassicMovies(
        isConnected: Boolean,
    ): Either<String, MovieResponse> {
        return if (isConnected) {
            safeApiCall(
                call = { movieApi.getClassicMovieAsync().await() },
                errorMessage = "Error Fetching Popular Movies"
            )

        } else errorConnection()
    }

    override fun getMoviePoster() = movieDao.getMovieList()

    private fun errorConnection(): Either<String, MovieResponse> =
        Either.Error("Device not connected")
}