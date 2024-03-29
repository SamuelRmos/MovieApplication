package com.example.movie.model

import com.example.movie.R

val sampleMovieData = listOf(
    Movie(
        id = 1,
        title = "Ad Astra",
        overview = "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
        posterImage = "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
        backDropImage = "/5BwqwxMEjeFtdknRV792Svo0K1v.jpg",
        releaseDate = "2024-01-18"
    ),
    Movie(
        id = 2,
        title = "Pulp Fiction",
        overview = "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
        posterImage = "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
        backDropImage = "/5BwqwxMEjeFtdknRV792Svo0K1v.jpg",
        releaseDate = "2024-01-18"
    ),
    Movie(
        id = 3,
        title = "Jurassic Park",
        overview = "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
        posterImage = "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
        backDropImage = "/5BwqwxMEjeFtdknRV792Svo0K1v.jpg",
        releaseDate = "2024-01-18"
    )
)

fun placeholderImage(id: Int): Int {
    val sampleImage = listOf(
        R.drawable.movie001,
        R.drawable.movie002,
        R.drawable.movie003
    )
    return sampleImage[Integer.min(id, sampleImage.size) - 1]
}