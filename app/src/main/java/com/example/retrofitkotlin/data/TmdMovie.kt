package com.example.retrofitkotlin.data

data class TmdMovie(
    val id: Int,
    val vote_average: Double,
    val title: String,
    val overview: String,
    val adult: Boolean
)

data class TmdbMovieResponse(
    val results: List<TmdMovie>
)