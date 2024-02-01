package com.example.movie.movie

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movie.model.Movie
import com.example.movie.model.SampleMovieData
import com.example.movie.theme.MovieTheme
import com.example.movie.theme.colorBackground
import com.example.movie.theme.colorText
import com.example.movie.viewmodel.ClassicMoviesViewModel
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.viewmodel.PopularMoviesViewModel
import com.example.movie.viewmodel.TodayMoviesViewModel

@Composable
fun MoviesScreen(
    ratedViewModel: MovieViewModel,
    todayViewModel: TodayMoviesViewModel,
    popularMoviesViewModel: PopularMoviesViewModel,
    classicMoviesViewModel: ClassicMoviesViewModel
) {
    Surface(color = colorBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            MoviesScreenCarousel(
                loading = ratedViewModel.uiState.loading,
                movies = ratedViewModel.uiState.moviesList,
                carouselTitle = "Rated Movies",
                modifier = Modifier.padding(top = 30.dp)
            )
            MoviesScreenCarousel(
                loading = todayViewModel.uiState.loading,
                movies = todayViewModel.uiState.moviesList,
                carouselTitle = "Today Movies"
            )
            MoviesScreenCarousel(
                loading = popularMoviesViewModel.uiState.loading,
                movies = popularMoviesViewModel.uiState.moviesList,
                carouselTitle = "Popular Movies"
            )
            MoviesScreenCarousel(
                loading = classicMoviesViewModel.uiState.loading,
                movies = classicMoviesViewModel.uiState.moviesList,
                carouselTitle = "Classic Movies",
            )
        }
    }
}
@Composable
fun MoviesScreenCarousel(
    modifier: Modifier = Modifier,
    loading: Boolean,
    movies: List<Movie>,
    carouselTitle: String
) {
    val listState = rememberLazyGridState()

    Box(modifier.height(280.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = modifier.padding(start = 20.dp),
                text = carouselTitle,
                style = MaterialTheme.typography.h5,
                color = colorText
            )
            MovieList(
                listState = listState,
                loading = loading,
                movie = movies
            )
        }
    }
}

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    listState: LazyGridState,
    loading: Boolean = false,
    movie: List<Movie>
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
                    MovieCard(movie = m)
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoviesScreenPreview() {
    MovieTheme {
        Surface(color = colorBackground) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                MoviesScreenCarousel(
                    loading = false,
                    movies = SampleMovieData,
                    carouselTitle = "Most Popular Movie"
                )
                MoviesScreenCarousel(
                    loading = false,
                    movies = SampleMovieData,
                    carouselTitle = "Rated Movie"
                )
            }
        }
    }
}