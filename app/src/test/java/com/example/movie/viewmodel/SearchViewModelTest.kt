package com.example.movie.viewmodel

import com.example.movie.model.Movie
import com.example.movie.model.SearchMovieItem
import com.example.movie.repository.SearchRepository
import com.example.movie.ui.details.DetailRequestState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchViewModelTest {

    private val repository = mockk<SearchRepository>()

    @Test
    fun onQueryChange_whenNonBlank_updatesStateWithResults() {
        val movie = Movie(1, "Run", "Overview", "/p.jpg", "/b.jpg", "2020-11-20")
        val items = listOf(SearchMovieItem(movie, "Aneesh Chaganty"))
        every { repository.searchMovies("run") } returns flowOf(
            DetailRequestState.Loading,
            DetailRequestState.Success(items)
        )

        val viewModel = SearchViewModel(repository, Dispatchers.Unconfined, debounceMs = 0L)
        viewModel.onQueryChange("run")

        val state = viewModel.searchState.value
        assertEquals("run", state.query)
        assertFalse(state.isLoading)
        assertEquals(items, state.results)
        assertEquals("", state.error)
    }

    @Test
    fun onQueryChange_whenBlank_clearsResultsWithoutCallingRepository() {
        val viewModel = SearchViewModel(repository, Dispatchers.Unconfined, debounceMs = 0L)
        viewModel.onQueryChange("   ")

        val state = viewModel.searchState.value
        assertEquals("   ", state.query)
        assertTrue(state.results.isEmpty())
        assertFalse(state.isLoading)
        verify(exactly = 0) { repository.searchMovies(any()) }
    }

    @Test
    fun onQueryChange_whenRepositoryEmitsError_updatesStateWithError() {
        every { repository.searchMovies("run") } returns flowOf(
            DetailRequestState.Loading,
            DetailRequestState.Error("Search failed")
        )

        val viewModel = SearchViewModel(repository, Dispatchers.Unconfined, debounceMs = 0L)
        viewModel.onQueryChange("run")

        val state = viewModel.searchState.value
        assertFalse(state.isLoading)
        assertTrue(state.results.isEmpty())
        assertEquals("Search failed", state.error)
    }
}
