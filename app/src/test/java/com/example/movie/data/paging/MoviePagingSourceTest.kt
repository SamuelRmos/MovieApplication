package com.example.movie.data.paging

import androidx.paging.PagingSource
import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class MoviePagingSourceTest {

    private fun createMovie(id: Int) = Movie(
        id = id,
        title = "Title $id",
        overview = "Overview $id",
        posterImage = "/poster$id.jpg",
        backDropImage = "/backdrop$id.jpg",
        releaseDate = "2026-01-01"
    )

    @Test
    fun load_initialPage_returnsSuccessWithNextKey() = runBlocking {
        val movies = (1..20).map { createMovie(it) }
        val pagingSource = MoviePagingSource(maxLimit = 300) { page ->
            assertEquals(1, page)
            Response.success(MovieResponse(results = movies))
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page<*, *>)
        val pageResult = result as PagingSource.LoadResult.Page<Int, Movie>
        assertEquals(movies, pageResult.data)
        assertNull(pageResult.prevKey)
        assertEquals(2, pageResult.nextKey)
    }

    @Test
    fun load_reachingMaxLimit_returnsNextKeyNull() = runBlocking {
        val movies = (1..20).map { createMovie(it) }
        // Page 15 * 20 = 300 >= maxLimit 300
        val pagingSource = MoviePagingSource(maxLimit = 300) { _: Int ->
            Response.success(MovieResponse(results = movies))
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Append(
                key = 15,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page<*, *>)
        val pageResult = result as PagingSource.LoadResult.Page<Int, Movie>
        assertEquals(movies, pageResult.data)
        assertNull(pageResult.nextKey)
    }

    @Test
    fun load_emptyResults_returnsNextKeyNull() = runBlocking {
        val pagingSource = MoviePagingSource(maxLimit = 300) { _: Int ->
            Response.success(MovieResponse(results = emptyList()))
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page<*, *>)
        val pageResult = result as PagingSource.LoadResult.Page<Int, Movie>
        assertTrue(pageResult.data.isEmpty())
        assertNull(pageResult.nextKey)
    }

    @Test
    fun load_httpError_returnsLoadResultError() = runBlocking {
        val errorResponseBody = "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        val pagingSource = MoviePagingSource(maxLimit = 300) { _: Int ->
            Response.error<MovieResponse>(500, errorResponseBody)
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error<*, *>)
    }

    @Test
    fun load_exception_returnsLoadResultError() = runBlocking {
        val pagingSource = MoviePagingSource(maxLimit = 300) { _: Int ->
            throw IOException("Network unavailable")
        }

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error<*, *>)
        val errorResult = result as PagingSource.LoadResult.Error<Int, Movie>
        assertTrue(errorResult.throwable is IOException)
    }
}
