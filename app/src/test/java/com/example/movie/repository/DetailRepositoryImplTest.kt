package com.example.movie.repository

import com.example.movie.model.Cast
import com.example.movie.model.Crew
import com.example.movie.model.MovieCredits
import com.example.movie.model.Video
import com.example.movie.model.VideosList
import com.example.movie.network.MovieApi
import com.example.movie.ui.details.DetailRequestState
import io.mockk.coEvery
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

class DetailRepositoryImplTest {

    private val movieApi = mockk<MovieApi>()
    private val repository = DetailRepositoryImpl(movieApi)

    @Test
    fun getMovieCredits_whenSuccessful_emitsLoadingThenSuccess() = runBlocking {
        val credits = MovieCredits(
            cast = listOf(Cast("Actor", "/path.jpg", "Acting")),
            crew = listOf(Crew("Director Name", null, "Director"))
        )
        coEvery { movieApi.getCreditsMovie(1) } returns Response.success(credits)

        val emissions = repository.getMovieCredits(1).toList()

        assertEquals(
            listOf(DetailRequestState.Loading, DetailRequestState.Success(credits)),
            emissions
        )
    }

    @Test
    fun getMovieVideo_whenSuccessful_emitsLoadingThenSuccess() = runBlocking {
        val videos = VideosList(
            results = listOf(
                Video(
                    "key123",
                    "Trailer",
                    key = ""
                )
            )
        )
        coEvery { movieApi.getVideosMovie(1) } returns Response.success(videos)

        val emissions = repository.getMovieVideo(1).toList()

        assertEquals(
            listOf(DetailRequestState.Loading, DetailRequestState.Success(videos)),
            emissions
        )
    }

    @Test
    fun getMovieCredits_whenHttpError_emitsLoadingThenError() = runBlocking {
        val errorBody = "Error".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { movieApi.getCreditsMovie(1) } returns Response.error(500, errorBody)

        val emissions = repository.getMovieCredits(1).toList()

        assertEquals(DetailRequestState.Loading, emissions[0])
        assertTrue(emissions[1] is DetailRequestState.Error)
    }

    @Test
    fun getMovieCredits_whenResponseBodyNull_emitsLoadingThenError() = runBlocking {
        coEvery { movieApi.getCreditsMovie(1) } returns Response.success(null)

        val emissions = repository.getMovieCredits(1).toList()

        assertEquals(DetailRequestState.Loading, emissions[0])
        assertTrue(emissions[1] is DetailRequestState.Error)
    }

    @Test
    fun getMovieCredits_whenExceptionThrown_emitsLoadingThenError() = runBlocking {
        coEvery { movieApi.getCreditsMovie(1) } throws IOException("Network failure")

        val emissions = repository.getMovieCredits(1).toList()

        assertEquals(DetailRequestState.Loading, emissions[0])
        assertTrue(emissions[1] is DetailRequestState.Error)
    }
}

