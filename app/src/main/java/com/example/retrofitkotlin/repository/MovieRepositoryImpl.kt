package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.network.TmdbApi
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.util.CategoryEnum

class MovieRepositoryImpl(private val movieApi: TmdbApi, private val movieDao: MovieDao) :
    BaseRepository(), MovieRepository {

    override suspend fun getListMovies(
        isConnected: Boolean,
        id: CategoryEnum
    ): MutableList<TmdMovie>? {
//        val dataBaseResponse = movieDao.getMovieList(id)
        return when {
            isConnected -> getDataMovie(id)
            else -> null
        }
    }

    private suspend fun getDataMovie(id: CategoryEnum): MutableList<TmdMovie>? {
        val dataReceived = getMovies(id)?.results?.toMutableList()
        movieDao.insertMovieList(dataReceived)
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