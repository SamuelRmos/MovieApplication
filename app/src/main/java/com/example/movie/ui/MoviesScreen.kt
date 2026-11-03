package com.example.movie.ui

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.R
import com.example.movie.navigation.Actions
import com.example.movie.theme.colorBackground
import com.example.movie.ui.components.ClassicMovieListRow
import com.example.movie.ui.components.CustomToolbar
import com.example.movie.ui.components.PopularMovieListRow
import com.example.movie.ui.components.RatedMovieListRow
import com.example.movie.ui.components.TodayMovieListRow
import com.example.movie.viewmodel.ClassicMoviesViewModel
import com.example.movie.viewmodel.PopularMoviesViewModel
import com.example.movie.viewmodel.RatedMovieViewModel
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
    val isSearchActive by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CustomToolbar(
                actions = actions,
                title = "CineBook",
                backgroundColor = colorBackground,
                isSearchActive = isSearchActive
            )
        },
        containerColor = colorBackground
    ) { innerPadding ->
        CarouselList(innerPadding, actions, todayViewModel)
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
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RatedMovieListRow(
            viewModel = ratedViewModel,
            carouselTitle = stringResource(R.string.most_watched_title),
            actions = actions
        )
        TodayMovieListRow(
            viewModel = todayViewModel,
            carouselTitle = stringResource(R.string.indications_today_title),
            actions = actions
        )
        PopularMovieListRow(
            viewModel = popularMoviesViewModel,
            carouselTitle = stringResource(R.string.upcoming_title),
            actions = actions
        )
        ClassicMovieListRow(
            viewModel = classicMoviesViewModel,
            carouselTitle = stringResource(R.string.carrousel_title_classics),
            actions = actions
        )
    }
}
