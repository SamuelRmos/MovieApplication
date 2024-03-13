package com.example.movie.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "poster_path")
    val posterImage: String,
    @Json(name = "backdrop_path")
    val backDropImage: String,
    @Json(name = "release_date")
    val releaseDate: String
): Parcelable

data class MovieResponse(
    val results: List<Movie>
)
