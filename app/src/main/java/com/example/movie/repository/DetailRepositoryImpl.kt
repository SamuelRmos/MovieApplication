package com.example.movie.repository

import com.example.movie.model.MovieCredits
import com.example.movie.network.MovieApi
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.annotations.VisibleForTesting
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_MESSAGE = "Error while fetching movies credits"

class DetailRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : DetailRepository {

    override suspend fun getMovieCredits(id: Int) = safeApiCall {
        movieApi.getCreditsMovie(id)
    }

    @VisibleForTesting
    internal suspend fun safeApiCall(
        call: suspend () -> Response<MovieCredits>
    ): Flow<DetailRequestState> = flow {
        try {
            emit(Loading)
            delay(1000)
            call().run {
                body()?.let {
                    if (isSuccessful) {
                        emit(Success(it))
                    } else {
                        emit(Error(ERROR_MESSAGE))
                    }
                } ?: emit(Error("Error response body is null"))
            }
        } catch (exception: Exception) {
            Timber.e(exception, exception.message)
            emit(Error(ERROR_MESSAGE))
        }
    }
}