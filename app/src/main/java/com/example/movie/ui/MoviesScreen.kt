package com.example.movie.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.movie.model.Movie
import com.example.movie.model.SampleMovieData
import com.example.movie.navigation.Screens.MovieDetails
import com.example.movie.navigation.navigate
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorBackground
import com.example.movie.theme.colorText
import com.example.movie.ui.components.CustomToolbarScreen
import com.example.movie.viewmodel.ClassicMoviesViewModel
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.viewmodel.PopularMoviesViewModel
import com.example.movie.viewmodel.TodayMoviesViewModel

@Composable
fun MoviesScreen(navController: NavHostController) {
    val ratedViewModel: MovieViewModel = hiltViewModel()
    val todayViewModel: TodayMoviesViewModel = hiltViewModel()
    val popularMoviesViewModel: PopularMoviesViewModel = hiltViewModel()
    val classicMoviesViewModel: ClassicMoviesViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            CustomToolbarScreen(
                navController = navController,
                title = "CineBook",
                colorBackground
            )
        },
        containerColor = colorBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MoviesScreenCarousel(
                loading = ratedViewModel.uiState.loading,
                movies = ratedViewModel.uiState.moviesList,
                carouselTitle = "Most Watched",
                navController = navController
            )
            MoviesScreenCarousel(
                loading = todayViewModel.uiState.loading,
                movies = todayViewModel.uiState.moviesList,
                carouselTitle = "Indications of Today",
                navController = navController
            )
            MoviesScreenCarousel(
                loading = popularMoviesViewModel.uiState.loading,
                movies = popularMoviesViewModel.uiState.moviesList,
                carouselTitle = "Populars",
                navController = navController
            )
            MoviesScreenCarousel(
                loading = classicMoviesViewModel.uiState.loading,
                movies = classicMoviesViewModel.uiState.moviesList,
                carouselTitle = "Classics",
                navController = navController
            )
        }
    }
}

@Composable
fun MoviesScreenCarousel(
    modifier: Modifier = Modifier,
    loading: Boolean,
    movies: List<Movie>,
    carouselTitle: String,
    navController: NavHostController
) {
    val listState = rememberLazyGridState()

    Box(modifier.height(280.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = modifier.padding(start = 20.dp),
                text = carouselTitle,
                style = MaterialTheme.typography.titleLarge,
                color = colorText
            )
            MovieList(
                listState = listState,
                loading = loading,
                movie = movies,
                navController = navController
            )
        }
    }
}

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    listState: LazyGridState,
    loading: Boolean = false,
    movie: List<Movie>,
    navController: NavHostController
) {
    val loaded = remember { MutableTransitionState(!loading) }
    LazyHorizontalGrid(
        modifier = modifier.height(220.dp),
        rows = GridCells.Fixed(1),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 20.dp)
    ) {
        if (loading) {
            item(span = { GridItemSpan(1) }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = Color.Black, modifier = Modifier.size(48.dp)
                    )
                }
            }
        } else {
            loaded.targetState = true

            itemsIndexed(movie) { idx, m ->
                AnimatedVisibility(
                    visibleState = loaded, enter = slideInHorizontally(
                        animationSpec = tween(500, idx / 2 * 120)
                    ) + fadeIn(
                        animationSpec = tween(400, idx / 2 * 150)
                    ), exit = ExitTransition.None
                ) {
                    MovieCard(
                        movie = m,
                        onMovieClick = { movie ->
                            navController.navigate(
                                route = MovieDetails.route,
                                Pair("movie_detail", movie)
                            )
                        })
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoviesScreenPreview() {
    val navController = rememberNavController()
    MovieTheme {
        Surface(color = colorBackground) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                MoviesScreenCarousel(
                    loading = false,
                    movies = SampleMovieData,
                    carouselTitle = "Most Popular Movie",
                    navController = navController
                )
                MoviesScreenCarousel(
                    loading = false,
                    movies = SampleMovieData,
                    carouselTitle = "Rated Movie",
                    navController = navController
                )
            }
        }
    }
}