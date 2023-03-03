package com.example.retrofitkotlin.movie

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.model.SampleMovieData
import com.example.retrofitkotlin.theme.MovieTheme
import com.example.retrofitkotlin.theme.colorBackground
import com.example.retrofitkotlin.theme.colorText

@Composable
fun MoviesScreenCarousel(loading: Boolean, movies: List<Movie>, carouselTitle: String) {
    val listState = rememberLazyGridState()

    Surface(color = colorBackground) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 30.dp)
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 25.dp),
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
        contentPadding = PaddingValues(start = 10.dp, top = 5.dp)
    ) {
        if (loading) {
            item(span = { GridItemSpan(1) }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        } else {
            loaded.targetState = true

            itemsIndexed(movie) { idx, m ->
                AnimatedVisibility(
                    visibleState = loaded,
                    enter = slideInHorizontally(
                        animationSpec = tween(500, idx / 2 * 120)
                    ) + fadeIn(
                        animationSpec = tween(400, idx / 2 * 150)
                    ),
                    exit = ExitTransition.None
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
        MoviesScreenCarousel(
            loading = false,
            movies = SampleMovieData,
            carouselTitle = "Most Popular Movie"
        )
    }
}