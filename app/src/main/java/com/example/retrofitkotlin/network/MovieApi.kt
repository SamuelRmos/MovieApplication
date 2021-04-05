package com.example.retrofitkotlin.network

import com.example.retrofitkotlin.model.TmdbMovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {

    @GET("movie/popular")
    fun getPopularMovieAsync(): Deferred<Response<TmdbMovieResponse>>

    @GET("movie/top_rated")
    fun getRatedMovieAsync(): Deferred<Response<TmdbMovieResponse>>

    @GET("trending/movie/day")
    fun getTodayMovieAsync(): Deferred<Response<TmdbMovieResponse>>

    @GET("discover/movie?language=en-US&page=1&sort_by=popularity.desc&release_date.gte=1940-01-01&release_date.lte=1999-12-31")
    fun getClassicMovieAsync(): Deferred<Response<TmdbMovieResponse>>
}