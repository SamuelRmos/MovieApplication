package com.example.retrofitkotlin.repository

import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.di.ApiComponent
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.service.TmdbApi
import retrofit2.Retrofit
import javax.inject.Inject

class MovieRepository : BaseRepository() {

    @Inject
    lateinit var retrofit: Retrofit

    init {
        val apiComponent: ApiComponent = MovieApplication.apiComponent
        apiComponent.inject(this)
    }

    suspend fun getPopularMovies(): MutableList<TmdMovie>? {

        val movieApi: TmdbApi = retrofit.create(TmdbApi::class.java)

        val movieResponse = safeApiCall(
            call = { movieApi.getPopularMovie().await() },
            errorMessage = "Error Fetching Popular Movies"
        )
        return movieResponse?.results?.toMutableList()
    }
}