package com.example.movie.navigation

import androidx.navigation.NavHostController
import com.example.movie.model.Movie
import com.example.movie.navigation.Screens.MovieDetails

class Actions(private val navHostController: NavHostController) {
    val goToMovieDetail: (Movie) -> Unit = {
        navHostController.navigate(
            route = MovieDetails.route,
            Pair("movie_detail", it)
        )
    }

    val navigateUp: () -> Unit = {
        navHostController.navigateUp()
    }
}