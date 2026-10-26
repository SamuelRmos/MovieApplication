package com.example.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movie.model.Movie
import com.example.movie.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodayMoviesViewModel @Inject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = movieRepository
        .getListTodayMovies()
        .cachedIn(viewModelScope)
}