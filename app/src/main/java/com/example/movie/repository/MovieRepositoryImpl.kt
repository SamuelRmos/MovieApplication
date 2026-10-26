package com.example.movie.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movie.data.paging.MoviePagingSource
import com.example.movie.model.Movie
import com.example.movie.network.MovieApi
import com.example.movie.ui.RequestState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : BaseRepository(), MovieRepository {
    override suspend fun getListPopularMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getPopularMovieAsync(page) }

    override suspend fun getListRatedMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getRatedMovieAsync(page) }

    override suspend fun getListTodayMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getTodayMovieAsync(page) }

    override suspend fun getListClassicMovies(page: Int): Flow<RequestState> =
        safeApiCall { movieApi.getClassicMovieAsync(page) }

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