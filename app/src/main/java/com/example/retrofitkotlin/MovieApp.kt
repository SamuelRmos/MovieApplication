package com.example.retrofitkotlin

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.retrofitkotlin.movie.MoviesScreen
import com.example.retrofitkotlin.viewmodel.MovieViewModel

@Composable
fun MovieApp() {
    MovieScreenRoute(viewModel = hiltViewModel())
}

@Composable
fun MovieScreenRoute(viewModel: MovieViewModel) {
    MoviesScreen(
        loading = viewModel.uiState.loading,
        movies = viewModel.uiState.movie
    )
}

