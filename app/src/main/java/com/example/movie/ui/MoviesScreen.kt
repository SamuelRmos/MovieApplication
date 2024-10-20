package com.example.movie.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.navigation.Actions
import com.example.movie.theme.colorBackground
import com.example.movie.ui.components.ClassicMovieListRow
import com.example.movie.ui.components.CustomToolbarScreen
import com.example.movie.ui.components.PopularMovieListRow
import com.example.movie.ui.components.RatedMovieListRow
import com.example.movie.ui.components.TodayMovieListRow
import com.example.movie.viewmodel.ClassicMoviesViewModel
import com.example.movie.viewmodel.RatedMovieViewModel
import com.example.movie.viewmodel.PopularMoviesViewModel
import com.example.movie.viewmodel.TodayMoviesViewModel

@Composable
fun MoviesScreen(actions: Actions, todayViewModel: TodayMoviesViewModel = hiltViewModel()) {
    val view = LocalView.current
    if (view.isInEditMode.not()) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()
        }
    }
    Scaffold(
        topBar = {
            CustomToolbarScreen(actions = actions, title = "CineBook", colorBackground)
        },
        containerColor = colorBackground
    ) { innerPadding ->
        val requestState by todayViewModel.stateTodayMovie.collectAsState()
        if (requestState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = White, modifier = Modifier.size(48.dp))
            }
        } else {
            CarouselList(innerPadding, actions, todayViewModel)
        }
    }
}

@Composable
private fun CarouselList(
    innerPadding: PaddingValues,
    actions: Actions,
    todayViewModel: TodayMoviesViewModel,
    ratedViewModel: RatedMovieViewModel = hiltViewModel(),
    popularMoviesViewModel: PopularMoviesViewModel = hiltViewModel(),
    classicMoviesViewModel: ClassicMoviesViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RatedMovieListRow(
            viewModel = ratedViewModel,
            carouselTitle = "Most Watched",
            actions = actions
        )
        TodayMovieListRow(
            viewModel = todayViewModel,
            carouselTitle = "Indications of Today",
            actions = actions
        )
        PopularMovieListRow(
            viewModel = popularMoviesViewModel,
            carouselTitle = "Populars",
            actions = actions
        )
        ClassicMovieListRow(
            viewModel = classicMoviesViewModel,
            carouselTitle = "Classics",
            actions = actions
        )
    }
}
