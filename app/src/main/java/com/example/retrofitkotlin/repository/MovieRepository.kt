package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.service.TmdbApi

class MovieRepository constructor(private val movieApi: TmdbApi) : BaseRepository() {

    suspend fun getPopularMovies(): MutableList<TmdMovie>? {

        val movieResponse = safeApiCall(
            call = { movieApi.getPopularMovie().await() },
            errorMessage = "Error Fetching Popular Movies"
        )
        return movieResponse?.results?.toMutableList()
    }
}