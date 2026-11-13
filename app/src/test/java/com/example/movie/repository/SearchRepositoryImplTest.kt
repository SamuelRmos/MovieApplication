package com.example.movie.repository

import com.example.movie.model.Crew
import com.example.movie.model.Movie
import com.example.movie.model.MovieCredits
import com.example.movie.model.MovieResponse
import com.example.movie.model.SearchMovieItem
import com.example.movie.network.MovieApi
import com.example.movie.ui.details.DetailRequestState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class SearchRepositoryImplTest {

    private val movieApi = mockk<MovieApi>()
    private val repository = SearchRepositoryImpl(movieApi)

    private val movie1 = Movie(1, "Run", "Overview 1", "/p1.jpg", "/b1.jpg", "2020-11-20")
    private val movie2 = Movie(2, "Run", "Overview 2", "/p2.jpg", "/b2.jpg", "1991-02-01")

    @Test
    fun searchMovies_whenSuccessful_enrichesWithDirectorsAndCachesCredits() = runBlocking {
        coEvery { movieApi.searchMovies("run") } returns Response.success(
            MovieResponse(listOf(movie1, movie2))
        )
        coEvery { movieApi.getCreditsMovie(1) } returns Response.success(
            MovieCredits(emptyList(), listOf(Crew("Aneesh Chaganty", null, "Director")))
        )
        coEvery { movieApi.getCreditsMovie(2) } returns Response.success(
            MovieCredits(emptyList(), listOf(Crew("Geoff Burrowes", null, "Director")))
        )

        val firstEmissions = repository.searchMovies("run").toList()
        val secondEmissions = repository.searchMovies("run").toList()

        val expectedItems = listOf(
            SearchMovieItem(movie1, "Aneesh Chaganty"),
            SearchMovieItem(movie2, "Geoff Burrowes")
        )
        assertEquals(
            listOf(DetailRequestState.Loading, DetailRequestState.Success(expectedItems)),
            firstEmissions
        )
        assertEquals(
            listOf(DetailRequestState.Loading, DetailRequestState.Success(expectedItems)),
            secondEmissions
        )
        coVerify(exactly = 1) { movieApi.getCreditsMovie(1) }
        coVerify(exactly = 1) { movieApi.getCreditsMovie(2) }
    }

    @Test
    fun searchMovies_whenCreditsCallFailsForOneMovie_fallsBackToEmptyDirector() = runBlocking {
        coEvery { movieApi.searchMovies("run") } returns Response.success(
            MovieResponse(listOf(movie1, movie2))
        )
        coEvery { movieApi.getCreditsMovie(1) } returns Response.success(
            MovieCredits(emptyList(), listOf(Crew("Aneesh Chaganty", null, "Director")))
        )
        coEvery { movieApi.getCreditsMovie(2) } throws IOException("Credits timeout")

        val emissions = repository.searchMovies("run").toList()

        val success = emissions[1] as DetailRequestState.Success
        assertEquals("Aneesh Chaganty", success.data[0].director)
        assertEquals("", success.data[1].director)
    }

    @Test
    fun searchMovies_whenHttpError_emitsLoadingThenError() = runBlocking {
        val errorBody = "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { movieApi.searchMovies("run") } returns Response.error(500, errorBody)

        val emissions = repository.searchMovies("run").toList()

        assertEquals(DetailRequestState.Loading, emissions[0])
        assertTrue(emissions[1] is DetailRequestState.Error)
    }
}

