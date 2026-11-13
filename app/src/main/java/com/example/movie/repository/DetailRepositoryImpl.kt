package com.example.movie.repository

import androidx.annotation.VisibleForTesting
import com.example.movie.model.MovieCredits
import com.example.movie.model.VideosList
import com.example.movie.network.MovieApi
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

private const val ERROR_MESSAGE = "Error while fetching movie details"
private const val EMPTY_BODY_ERROR_MESSAGE = "Error response body is null"

class DetailRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : DetailRepository {

    override fun getMovieCredits(id: Int): Flow<DetailRequestState<MovieCredits>> =
        safeApiCall { movieApi.getCreditsMovie(id) }

    override fun getMovieVideo(id: Int): Flow<DetailRequestState<VideosList>> =
        safeApiCall { movieApi.getVideosMovie(id) }

    @VisibleForTesting
    internal fun <T> safeApiCall(
        call: suspend () -> Response<T>
    ): Flow<DetailRequestState<T>> = flow {
        emit(Loading)
        try {
            val response = call()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(Success(body))
            } else {
                emit(Error(if (body == null && response.isSuccessful) EMPTY_BODY_ERROR_MESSAGE else ERROR_MESSAGE))
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Timber.e(exception)
            emit(Error(ERROR_MESSAGE))
        }
    }.flowOn(Dispatchers.IO)
}
