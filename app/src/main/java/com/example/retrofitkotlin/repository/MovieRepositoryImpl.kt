package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.network.MovieApi
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.util.CategoryEnum
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) : BaseRepository(), MovieRepository {

    override suspend fun getListMovies(
        isConnected: Boolean,
        id: CategoryEnum
    ): MutableList<TmdMovie>? {
        return when {
            isConnected -> getDataMovie(id)
            else -> null
        }
    }

    override fun getMoviePoster() = movieDao.getMovieList()

    private suspend fun getDataMovie(id: CategoryEnum): MutableList<TmdMovie>? {
        val dataReceived = getMovies(id)?.results?.toMutableList()
        if (id == CategoryEnum.TODAY) {
            movieDao.insertMovieList(dataReceived)
        }
        return dataReceived
    }

    private suspend fun getMovies(id: CategoryEnum) = when (id) {
        CategoryEnum.POPULAR -> {
            safeApiCall(
                call = { movieApi.getPopularMovieAsync().await() },
                errorMessage = "Error Fetching Popular Movies"
            )
        }
        CategoryEnum.RATED -> {
            safeApiCall(
                call = { movieApi.getRatedMovieAsync().await() },
                errorMessage = "Error Fetching Rated Movies"
            )
        }
        CategoryEnum.TODAY -> {
            safeApiCall(
                call = { movieApi.getTodayMovieAsync().await() },
                errorMessage = "Error Fetching Today Movies"
            )
        }
        CategoryEnum.CLASSIC -> {
            safeApiCall(
                call = { movieApi.getClassicMovieAsync().await() },
                errorMessage = "Error Fetching Classic Movies"
            )
        }
    }
}