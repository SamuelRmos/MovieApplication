package com.example.retrofitkotlin.data

data class TmdMovie(
    val id: Int,
    val vote_average: Double,
    val title: String,
    val overview: String,
    val adult: Boolean,
    val poster_path: String,
    val release_date: String,
    val backdrop_path: String
)

data class TmdbMovieResponse(
    val results: List<TmdMovie>
)