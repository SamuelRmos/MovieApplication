package com.example.movie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse
import retrofit2.Response

const val DEFAULT_MOVIES_LIMIT = 300

class MoviePagingSource(
    private val maxLimit: Int = DEFAULT_MOVIES_LIMIT,
    private val fetchMovies: suspend (page: Int) -> Response<MovieResponse>
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = fetchMovies(page)
            if (response.isSuccessful && response.body() != null) {
                val movies = response.body()!!.results
                val nextKey = if (movies.isEmpty() || (page * params.loadSize) >= maxLimit) {
                    null
                } else {
                    page + 1
                }
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception("Error while fetching movies data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
