package com.example.retrofitkotlin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.retrofitkotlin.movie.MoviesScreen
import com.example.retrofitkotlin.view.viewmodel.MovieViewModel

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

