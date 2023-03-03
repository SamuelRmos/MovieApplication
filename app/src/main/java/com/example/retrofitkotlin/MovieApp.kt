package com.example.retrofitkotlin

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.retrofitkotlin.movie.MoviesScreen

@Composable
fun MovieApp() {
    MoviesScreen(
        ratedViewModel = hiltViewModel(),
        todayViewModel = hiltViewModel(),
        popularMoviesViewModel = hiltViewModel(),
        classicMoviesViewModel = hiltViewModel()
    )
}

