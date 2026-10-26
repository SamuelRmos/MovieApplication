package com.example.movie.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.movie.navigation.Actions
import com.example.movie.theme.colorText
import com.example.movie.ui.MovieCard
import com.example.movie.viewmodel.RatedMovieViewModel

@Composable
fun RatedMovieListRow(
    modifier: Modifier = Modifier,
    viewModel: RatedMovieViewModel,
    carouselTitle: String,
    actions: Actions
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
            RatedMovieList(
                viewModel = viewModel,
                actions = actions
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RatedMovieList(
    modifier: Modifier = Modifier,
    viewModel: RatedMovieViewModel,
    actions: Actions,
) {
    val lazyListState = rememberLazyListState()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val focusManger = LocalFocusManager.current
    val isRefreshing = movies.loadState.refresh is LoadState.Loading && movies.itemCount > 0
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { movies.refresh() }
    )

    val refreshState = movies.loadState.refresh
    val isInitialLoading = refreshState is LoadState.Loading && movies.itemCount == 0
    val isInitialWaiting = movies.itemCount == 0 && refreshState !is LoadState.Error && !refreshState.endOfPaginationReached
    val showShimmer = isInitialLoading || isInitialWaiting

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
        when {
            refreshState is LoadState.Error && movies.itemCount == 0 -> {
                ErrorRow(movies)
            }
            showShimmer -> {
                MovieRowShimmer(modifier = modifier)
            }
            else -> {
                LazyRow(
                    modifier = modifier.height(185.dp),
                    state = lazyListState,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(start = 10.dp, end = 10.dp)
                ) {
                    items(
                        count = movies.itemCount,
                        key = movies.itemKey { it.id }
                    ) { index ->
                        val movie = movies[index]
                        if (movie?.posterImage != null) {
                            MovieCard(
                                modifier = modifier.width(150.dp),
                                movie = movie,
                                onMovieClick = { actions.goToMovieDetail(it) }
                            )
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}