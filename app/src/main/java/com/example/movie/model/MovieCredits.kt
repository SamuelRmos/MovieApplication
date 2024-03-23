package com.example.movie.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class MovieCredits(
    val cast: List<Cast>,
    val crew: List<Crew>
)

@JsonClass(generateAdapter = true)
data class Cast(
    val name: String,
    @Json(name = "profile_path")
    val imageProfile: String?,
    @Json(name = "known_for_department")
    val job: String
)

@JsonClass(generateAdapter = true)
data class Crew(
    val name: String,
    @Json(name = "profile_path")
    val imageProfile: String?,
    val job: String
)