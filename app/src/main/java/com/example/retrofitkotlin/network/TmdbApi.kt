package com.example.retrofitkotlin.network

import com.example.retrofitkotlin.model.TmdbMovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface TmdbApi {

    @GET("movie/popular")
    fun getPopularMovieAsync(): Deferred<Response<TmdbMovieResponse>>
}