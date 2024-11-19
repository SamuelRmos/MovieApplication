package com.example.movie.navigation

sealed class Screens(val route: String) {
    data object MoviesScreen: Screens("movie_screen")
    data object MovieDetails: Screens("detail_screen")
}