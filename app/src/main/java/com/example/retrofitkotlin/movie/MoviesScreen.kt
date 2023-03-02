package com.example.retrofitkotlin.movie

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.model.SampleMovieData
import com.example.retrofitkotlin.theme.MovieTheme
import com.example.retrofitkotlin.theme.colorBackground

@Composable
fun MoviesScreen(loading: Boolean, movies: List<Movie>) {
    val listState = rememberLazyGridState()

//    val topAppBarRevealThreshold = with(LocalDensity.current) { 64.dp.toPx() }
//    val scrollOffset by remember {
//        derivedStateOf {
//            listState.firstVisibleItemScrollOffset
//        }
//    }
//
//    val isGridAtTop by remember {
//        derivedStateOf {
//            scrollOffset < topAppBarRevealThreshold && listState.firstVisibleItemIndex == 0
//        }
//    }
//
//    val topAppBarTitleRevealProgress by remember {
//        derivedStateOf {
//            if (isGridAtTop) {
//                1f - (topAppBarRevealThreshold - scrollOffset) / topAppBarRevealThreshold
//            } else {
//                1f
//            }
//        }
//    }
//
//    val backgroundScrollThreshold = with(LocalDensity.current) { 40.dp.toPx() }
//    val backgroundRevealProgress by remember {
//        derivedStateOf {
//            if (isGridAtTop) {
//                (1f - (backgroundScrollThreshold - scrollOffset) / backgroundScrollThreshold).coerceIn(
//                    0f,
//                    1f
//                )
//            } else {
//                1f
//            }
//        }
//    }

    Surface(color = colorBackground) {
        Box(Modifier.fillMaxSize()) {
            MovieList(
                modifier = Modifier.statusBarsPadding(),
                listState = listState,
                loading = loading,
                title = {
                    Text(
                        text = "Most Popular Movie",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.padding(top = 5.dp, bottom = 12.dp)
                    )
                },
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
    title: @Composable () -> Unit,
    movie: List<Movie>
) {
    val loaded = remember { MutableTransitionState(!loading) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 64.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            title()
        }

        if (loading) {
            item(span = { GridItemSpan(2) }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(vertical = 24.dp)
                    )
                }
            }
        } else {
            loaded.targetState = true

            itemsIndexed(movie) { idx, m ->
                AnimatedVisibility(
                    visibleState = loaded,
                    enter = slideInVertically(
                        animationSpec = tween(500, idx / 2 * 120),
                        initialOffsetY = { it / 2 }
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
        MoviesScreen(
            loading = false,
            movies = SampleMovieData
        )
    }
}