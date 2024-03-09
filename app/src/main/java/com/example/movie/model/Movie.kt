package com.example.movie.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "poster_path")
    val posterImage: String,
    @Json(name = "backdrop_path")
    val backDropImage: String
)

data class MovieResponse(
    val results: List<Movie>
)
