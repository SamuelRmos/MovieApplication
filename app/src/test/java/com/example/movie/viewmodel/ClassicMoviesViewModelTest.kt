package com.example.movie.viewmodel

import androidx.paging.PagingData
import com.example.movie.model.Movie
import com.example.movie.repository.MovieRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotNull
import org.junit.Test

class ClassicMoviesViewModelTest {

    private val repository = mockk<MovieRepository>()

    @Test
    fun movies_initializesFromRepository() {
        val pagingData = PagingData.empty<Movie>()
        every { repository.getClassicMoviesPaging() } returns flowOf(pagingData)

        val viewModel = ClassicMoviesViewModel(repository)

        assertNotNull(viewModel.movies)
        verify(exactly = 1) { repository.getClassicMoviesPaging() }
    }
}
