package com.example.movie.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.MovieCredits
import com.example.movie.repository.DetailRepository
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import com.example.movie.ui.state.DetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val EMPTY_STRING = ""

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val detailRepository: DetailRepository) :
    ViewModel() {

    private var _stateDetails = MutableStateFlow(DetailsState())
    val stateDetails = _stateDetails.asStateFlow()

    fun getMovieCredits(id: Int) {
        viewModelScope.launch(IO) {
            detailRepository.getMovieCredits(id)
                .distinctUntilChanged()
                .collectLatest { result ->
                    when (result) {
                        is Success -> onSuccess(result.credits)
                        is Error ->  onRequestError(result.message)
                        is Loading -> onRequestLoading()
                    }
                }
        }
    }

    private fun onRequestLoading() {
        if (_stateDetails.value.director.isBlank()) {
            _stateDetails.update {
                it.copy(isLoading = true)
            }
        }
    }

    private fun onSuccess(movieCredits: MovieCredits) {
        val crew = movieCredits.crew.firstOrNull { it.job == "Director" }
        _stateDetails.update {
            it.copy(
                director = crew?.name ?: EMPTY_STRING,
                isLoading = false
            )
        }
    }

    @VisibleForTesting
    internal fun onRequestError(message: String?) {
        _stateDetails.update {
            it.copy(error = message ?: "Unexpected Error", isLoading = false)
        }
    }
}