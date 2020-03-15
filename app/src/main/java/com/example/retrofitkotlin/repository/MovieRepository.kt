package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.service.RetrofitFactory
import com.example.retrofitkotlin.service.TmdbApi
import com.example.retrofitkotlin.util.Constants

class MovieRepository(private val api : TmdbApi) : BaseRepository() {



        suspend fun getPopularMovies() : MutableList<TmdMovie>?{

        val movieResponse = safeApiCall(
            call = {api.getPopularMovie().await()},
            errorMessage = "Error Fetching Popular Movies"
        )
        return movieResponse?.results?.toMutableList();
    }
}