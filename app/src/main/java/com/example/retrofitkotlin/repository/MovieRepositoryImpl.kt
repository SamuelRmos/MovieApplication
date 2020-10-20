package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.network.TmdbApi
import com.example.retrofitkotlin.persistence.MovieDao

class MovieRepositoryImpl(private val movieApi: TmdbApi, private val movieDao: MovieDao) :
    BaseRepository(), MovieRepository {

    private val movieList = movieDao.getMovieList()

    override suspend fun getPopularMovies(isConnected: Boolean) = when {
        movieList.size == 0 || isConnected -> dataFetchLogic()
        else -> movieList
    }

    private suspend fun dataFetchLogic(): MutableList<TmdMovie>? {

        val movieResponse = safeApiCall(
            call = { movieApi.getPopularMovieAsync().await() },
            errorMessage = "Error Fetching Popular Movies"
        )

        val dataReceived = movieResponse?.results?.toMutableList()
        movieDao.insertMovieList(dataReceived)

        return dataReceived
    }
}