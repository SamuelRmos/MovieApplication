package com.example.movie.navigation

import androidx.navigation.NavHostController
import com.example.movie.model.Movie
import com.example.movie.navigation.Screens.MovieDetails
import com.example.movie.navigation.Screens.MoviesScreen

class Actions(private val navHostController: NavHostController) {
    val goToMovieDetail: (Movie) -> Unit = {
        navHostController.navigate(
            route = MovieDetails.route,
            Pair("movie_detail", it)
        )
    }

    val goToMovies: () -> Unit = {
        navHostController.navigate(MoviesScreen.route)
    }

    val navigateUp: () -> Unit = {
        navHostController.navigateUp()
    }
}