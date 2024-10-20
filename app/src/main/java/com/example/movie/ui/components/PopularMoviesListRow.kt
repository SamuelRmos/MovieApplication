package com.example.movie.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.movie.navigation.Actions
import com.example.movie.theme.colorText
import com.example.movie.ui.MovieCard
import com.example.movie.viewmodel.PopularMoviesViewModel

@Composable
fun PopularMovieListRow(
    modifier: Modifier = Modifier,
    viewModel: PopularMoviesViewModel,
    carouselTitle: String,
    actions: Actions,
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
            PopularMovieList(viewModel = viewModel, actions = actions)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PopularMovieList(
    modifier: Modifier = Modifier,
    viewModel: PopularMoviesViewModel,
    actions: Actions
) {
    val lazyListState = rememberLazyListState()
    val requestState by viewModel.statePopularMovie.collectAsState()
    val paginationState by viewModel.paginationPopularState.collectAsState()
    val isRefreshing by viewModel.isRefresh.collectAsState()

    val focusManger = LocalFocusManager.current
    val loaded = remember { MutableTransitionState(requestState.isLoading) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = viewModel::refresh
    )

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress) {
            focusManger.clearFocus()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshState)
    ) {
        LazyRow(
            modifier = modifier.height(185.dp),
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
        ) {
            if (requestState.isLoading.not()) {
                loaded.targetState = true
                itemsIndexed(requestState.movies) { _, movie ->
                    movie.posterImage?.let {
                        AnimatedVisibility(
                            visibleState = loaded
                        ) {
                            MovieCard(
                                modifier = modifier.width(150.dp),
                                movie = movie,
                                onMovieClick = { actions.goToMovieDetail(it) }
                            )
                        }
                    }
                }
            }
            item {
                if (paginationState.isLoading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = White, modifier = Modifier.size(48.dp))
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = requestState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        InfiniteListHandler(lazyListState = lazyListState) {
            viewModel.getMoviesPaginated()
        }
    }
}