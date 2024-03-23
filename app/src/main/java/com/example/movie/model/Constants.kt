package com.example.movie.model

import com.example.movie.BuildConfig

object Constants {
    const val tmdbApiKey = BuildConfig.TMDB_API_KEY
    const val baseURL = "https://api.themoviedb.org/3/"
    private const val baseUrlImage = "https://image.tmdb.org/t/p/w500"
    private const val baseDetailPosterImage = "https://image.tmdb.org/t/p/w300"
    private const val baseImageBack = "https://image.tmdb.org/t/p/w1280"

    fun artworkUrl(image: String): String = baseImageBack + image

    fun artworkDetailImagePoster(image: String): String = baseDetailPosterImage + image

    fun artworkImagePoster(image: String): String = baseUrlImage + image
}