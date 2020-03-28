package com.example.retrofitkotlin.util

import com.example.retrofitkotlin.BuildConfig

object Constants {
    var tmdbApiKey = BuildConfig.TMDB_API_KEY
    var baseURL = "https://api.themoviedb.org/3/"
    var baseUrlImage = "https://image.tmdb.org/t/p/w200"
    var baseImageBack = "https://image.tmdb.org/t/p/w500"
}