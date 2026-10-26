package com.example.movie.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movie.data.paging.MoviePagingSource
import com.example.movie.model.Movie
import com.example.movie.network.MovieApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : BaseRepository(), MovieRepository {

    override fun getListPopularMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource { page -> movieApi.getPopularMovieAsync(page) }
            }
        ).flow

    override fun getListRatedMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource { page -> movieApi.getRatedMovieAsync(page) }
            }
        ).flow

    override fun getListTodayMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource { page -> movieApi.getTodayMovieAsync(page) }
            }
        ).flow

    override fun getClassicMoviesPaging(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            MoviePagingSource { page -> movieApi.getClassicMovieAsync(page) }
        }
    ).flow
}