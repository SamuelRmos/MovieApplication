package com.example.movie

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.movie.MoviesScreen

@Composable
fun MovieApp() {
    MoviesScreen(
        ratedViewModel = hiltViewModel(),
        todayViewModel = hiltViewModel(),
        popularMoviesViewModel = hiltViewModel(),
        classicMoviesViewModel = hiltViewModel()
    )
}