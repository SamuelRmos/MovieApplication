package com.example.movie.model

import com.example.movie.BuildConfig

object Constants {
    const val API_KEY = BuildConfig.TMDB_API_KEY
    const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"
    private const val POSTER_IMAGE = "https://image.tmdb.org/t/p/w300"
    private const val BASE_IMAGE = "https://image.tmdb.org/t/p/w1280"

    fun artworkUrl(image: String): String = BASE_IMAGE + image

    fun artworkDetailImagePoster(image: String): String = POSTER_IMAGE + image

    fun artworkImagePoster(image: String): String = BASE_URL_IMAGE + image
}