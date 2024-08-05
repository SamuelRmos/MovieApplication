package com.example.movie.ui

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.navigation.Actions
import com.example.movie.theme.colorBackground
import com.example.movie.theme.colorText
import com.example.movie.ui.components.CustomToolbarScreen
import com.example.movie.viewmodel.ClassicMoviesViewModel
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.viewmodel.PopularMoviesViewModel
import com.example.movie.viewmodel.TodayMoviesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    actions: Actions,
    ratedViewModel: MovieViewModel = hiltViewModel(),
    todayViewModel: TodayMoviesViewModel = hiltViewModel(),
    popularMoviesViewModel: PopularMoviesViewModel = hiltViewModel(),
    classicMoviesViewModel: ClassicMoviesViewModel = hiltViewModel()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
        }
    }
    Scaffold(
        topBar = {
            CustomToolbarScreen(
                actions = actions,
                title = "CineBook",
                colorBackground
            )
        },
        containerColor = colorBackground
    ) { innerPadding ->
        val requestState by classicMoviesViewModel.requestState
        if (requestState.isLoading()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.White, modifier = Modifier.size(48.dp)
                )
            }
        } else {
            CarouselList(
                innerPadding,
                ratedViewModel,
                actions,
                todayViewModel,
                popularMoviesViewModel,
                classicMoviesViewModel
            )
        }
    }
}

@Composable
private fun CarouselList(
    innerPadding: PaddingValues,
    ratedViewModel: MovieViewModel,
    actions: Actions,
    todayViewModel: TodayMoviesViewModel,
    popularMoviesViewModel: PopularMoviesViewModel,
    classicMoviesViewModel: ClassicMoviesViewModel
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        MoviesScreenCarousel(
            requestState = ratedViewModel.requestState.value,
            carouselTitle = "Most Watched",
            actions = actions
        )
        MoviesScreenCarousel(
            requestState = todayViewModel.requestState.value,
            carouselTitle = "Indications of Today",
            actions = actions
        )
        MoviesScreenCarousel(
            requestState = popularMoviesViewModel.requestState.value,
            carouselTitle = "Populars",
            actions = actions
        )
        MoviesScreenCarousel(
            requestState = classicMoviesViewModel.requestState.value,
            carouselTitle = "Classics",
            actions = actions
        )
    }
}

@Composable
fun MoviesScreenCarousel(
    modifier: Modifier = Modifier,
    requestState: RequestState,
    carouselTitle: String,
    actions: Actions
) {
    Box(modifier.height(220.dp).padding(bottom = 12.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = modifier.padding(start = 10.dp),
                text = carouselTitle,
                style = MaterialTheme.typography.titleLarge,
                color = colorText
            )
            MovieList(requestState = requestState, actions = actions)
        }
    }
}

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    requestState: RequestState,
    actions: Actions
) {
    val listState = rememberLazyGridState()
    val loaded = remember { MutableTransitionState(requestState.isLoading()) }
    LazyHorizontalGrid(
        modifier = modifier.height(185.dp),
        rows = GridCells.Fixed(1),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
    ) {
        if (requestState.isSuccess()) {
            loaded.targetState = true
            itemsIndexed(requestState.getMovieList()) { idx, m ->
                AnimatedVisibility(
                    visibleState = loaded,
                    enter = slideInHorizontally(
                        animationSpec = tween(500, idx / 2 * 120)
                    ) + fadeIn(
                        animationSpec = tween(400, idx / 2 * 150)
                    ), exit = ExitTransition.None
                ) {
                    MovieCard(
                        modifier = modifier.width(150.dp),
                        movie = m,
                        onMovieClick = { actions.goToMovieDetail(it) }
                    )
                }
            }
        }
    }
}