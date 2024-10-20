package com.example.movie.network

import com.example.movie.model.MovieCredits
import com.example.movie.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovieAsync(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getRatedMovieAsync(@Query("page") page: Int): Response<MovieResponse>

    @GET("trending/movie/day")
    suspend fun getTodayMovieAsync(@Query("page") page: Int): Response<MovieResponse>

    @GET("discover/movie?language=en-US&page=1&sort_by=popularity.desc&release_date.gte=1940-01-01&release_date.lte=2010-12-31")
    suspend fun getClassicMovieAsync(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/{id}/credits")
    suspend fun getCreditsMovie(@Path("id") id: Int): Response<MovieCredits>
}