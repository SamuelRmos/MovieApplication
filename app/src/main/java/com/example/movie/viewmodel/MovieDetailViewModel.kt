package com.example.movie.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.MovieCredits
import com.example.movie.model.VideosList
import com.example.movie.repository.DetailRepository
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import com.example.movie.ui.state.DetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val DIRECTOR_JOB = "Director"
private const val TRAILER_TYPE = "Trailer"
private const val DEFAULT_ERROR_MESSAGE = "Unexpected Error"

@HiltViewModel
class MovieDetailViewModel(
    private val detailRepository: DetailRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    @Inject
    constructor(detailRepository: DetailRepository) : this(detailRepository, Dispatchers.IO)

    private val _stateDetails = MutableStateFlow(DetailsState())
    val stateDetails = _stateDetails.asStateFlow()

    private var currentMovieId: Int? = null
    private var fetchJob: Job? = null

    fun getMovieCredits(id: Int) {
        if (currentMovieId == id && _stateDetails.value.error.isEmpty()) return
        currentMovieId = id
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(dispatcher) {
            combine(
                detailRepository.getMovieCredits(id).distinctUntilChanged(),
                detailRepository.getMovieVideo(id).distinctUntilChanged(),
                ::mergeStates
            ).collectLatest { newState ->
                _stateDetails.value = newState
            }
        }
    }

    private fun mergeStates(
        creditsState: DetailRequestState<MovieCredits>,
        videoState: DetailRequestState<VideosList>
    ): DetailsState {
        val current = _stateDetails.value
        val isLoading = creditsState is Loading || videoState is Loading

        val director = when (creditsState) {
            is Success -> creditsState.data.crew
                .firstOrNull { it.job == DIRECTOR_JOB }
                ?.name
                .orEmpty()
            else -> current.director
        }

        val cast = when (creditsState) {
            is Success -> creditsState.data.cast
            else -> current.cast
        }

        val trailerKey = when (videoState) {
            is Success -> videoState.data.results
                .firstOrNull { it.type == TRAILER_TYPE }
                ?.key
                .orEmpty()
            else -> current.key
        }

        val error = when {
            creditsState is Error -> creditsState.message
            videoState is Error -> videoState.message
            else -> ""
        }

        return DetailsState(
            director = director,
            cast = cast,
            isLoading = isLoading,
            error = error,
            key = trailerKey
        )
    }

    @VisibleForTesting
    internal fun onRequestError(message: String?) {
        _stateDetails.update {
            it.copy(error = message ?: DEFAULT_ERROR_MESSAGE, isLoading = false)
        }
    }
}
