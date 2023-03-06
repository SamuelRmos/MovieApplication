package com.example.movie.network

import com.example.movie.model.MovieResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface MovieApi {

    @GET("movie/popular")
    fun getPopularMovieAsync(): Deferred<Response<MovieResponse>>

    @GET("movie/top_rated")
    fun getRatedMovieAsync(): Deferred<Response<MovieResponse>>

    @GET("trending/movie/day")
    fun getTodayMovieAsync(): Deferred<Response<MovieResponse>>

    @GET("discover/movie?language=en-US&page=1&sort_by=popularity.desc&release_date.gte=1940-01-01&release_date.lte=1998-12-31")
    fun getClassicMovieAsync(): Deferred<Response<MovieResponse>>
}