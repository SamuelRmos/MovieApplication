package com.example.movie.navigation

sealed class Screens(val route: String) {
    object MoviesScreen: Screens("movie_screen")
    object MovieDetails: Screens("detail_screen")
}