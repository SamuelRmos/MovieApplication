package com.example.movie.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.core.os.BundleCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movie.model.Movie
import com.example.movie.navigation.Screens.MovieDetails
import com.example.movie.navigation.Screens.MoviesScreen
import com.example.movie.ui.details.MovieDetailsScreen
import com.example.movie.ui.MoviesScreen

@Composable
fun NavGraph(navController: NavHostController, actions: Actions) {
    NavHost(navController = navController, startDestination = MoviesScreen.route) {
        composable(route = MoviesScreen.route) {
            MoviesScreen(actions)
        }
        composable(route = MovieDetails.route) {
            val movie = it.requiredArg<Movie>("movie_detail")
            MovieDetailsScreen(movie, actions)
        }
    }
}

fun NavController.navigate(route: String, vararg args: Pair<String, Parcelable>) {
    navigate(route)
    requireNotNull(currentBackStackEntry?.arguments).apply {
        args.forEach { (key: String, arg: Parcelable) ->
            putParcelable(key, arg)
        }
    }
}

inline fun <reified T : Parcelable> NavBackStackEntry.requiredArg(key: String): T =
    requireNotNull(arguments) { "arguments bundle is null" }.run {
        requireNotNull(getParcel(key)) { "argument for $key is null" }
    }

inline fun <reified T: Parcelable> Bundle.getParcel(key: String): T? =
    BundleCompat.getParcelable(this, key, T::class.java)