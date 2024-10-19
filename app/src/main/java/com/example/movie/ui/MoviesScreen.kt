package com.example.movie.ui

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
import com.example.movie.viewmodel.BaseViewModel
import com.example.movie.viewmodel.ClassicMoviesViewModel
import com.example.movie.viewmodel.MovieViewModel
import com.example.movie.viewmodel.PopularMoviesViewModel
import com.example.movie.viewmodel.TodayMoviesViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

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
            viewModel = ratedViewModel,
            carouselTitle = "Most Watched",
            loadMore = {
                var page = 1
                page++
                ratedViewModel.fetchMovies(page)
            },
            actions = actions
        )
        MoviesScreenCarousel(
            viewModel = todayViewModel,
            carouselTitle = "Indications of Today",
            loadMore = {
                var page = 1
                page++
                todayViewModel.fetchMovies(page)
            },
            actions = actions
        )
        MoviesScreenCarousel(
            viewModel = popularMoviesViewModel,
            loadMore = {
                var page = 1
                page++
                popularMoviesViewModel.fetchMovies(page)
            },
            carouselTitle = "Populars",
            actions = actions
        )
        MoviesScreenCarousel(
            viewModel = classicMoviesViewModel,
            carouselTitle = "Classics",
            loadMore = {
                var page = 1
                page++
                classicMoviesViewModel.fetchMovies(page)
            },
            actions = actions
        )
    }
}

@Composable
fun MoviesScreenCarousel(
    modifier: Modifier = Modifier,
    viewModel: BaseViewModel,
    carouselTitle: String,
    actions: Actions,
    loadMore: () -> Unit,
) {
    Box(
        modifier
            .height(220.dp)
            .padding(bottom = 12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = modifier.padding(start = 10.dp),
                text = carouselTitle,
                style = MaterialTheme.typography.titleLarge,
                color = colorText
            )
            MovieList(viewModel = viewModel, loadMore = loadMore, actions = actions)
        }
    }
}

@Composable
fun MovieList(
    modifier: Modifier = Modifier,
    viewModel: BaseViewModel,
    loadMore: () -> Unit,
    actions: Actions,
    buffer: Int = 2
) {
    val listState = rememberLazyListState()
    val requestState by viewModel.requestState
    val loaded = remember { MutableTransitionState(requestState.isLoading()) }
    val load = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleItemIndex < (totalItems - buffer)
        }
    }
    LaunchedEffect(load) {
        snapshotFlow { load.value }
            .distinctUntilChanged()
            .collect {
                loadMore()
            }
    }
    LazyRow(
        modifier = modifier.height(185.dp),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
    ) {
        if (requestState.isSuccess()) {
            loaded.targetState = true
            itemsIndexed(requestState.getMovieList()) { idx, m ->
                AnimatedVisibility(
                    visibleState = loaded
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