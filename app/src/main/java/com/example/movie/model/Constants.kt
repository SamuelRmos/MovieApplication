package com.example.movie.model

import com.example.movie.BuildConfig

object Constants {
    var tmdbApiKey = BuildConfig.TMDB_API_KEY
    var baseURL = "https://api.themoviedb.org/3/"
    var baseUrlImage = "https://image.tmdb.org/t/p/w500"
    var baseImageBack = "https://image.tmdb.org/t/p/original"

    fun artworkUrl(image: String): String = baseImageBack + image
}