package com.example.movie.repository

import com.example.movie.model.Movie
import com.example.movie.model.SearchMovieItem
import com.example.movie.network.MovieApi
import com.example.movie.ui.details.DetailRequestState
import com.example.movie.ui.details.DetailRequestState.Error
import com.example.movie.ui.details.DetailRequestState.Loading
import com.example.movie.ui.details.DetailRequestState.Success
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

private const val DIRECTOR_JOB = "Director"
private const val SEARCH_ERROR_MESSAGE = "Error while searching movies"

class SearchRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : SearchRepository {

    private val directorCache = ConcurrentHashMap<Int, String>()

    override fun searchMovies(query: String): Flow<DetailRequestState<List<SearchMovieItem>>> = flow {
        emit(Loading)
        try {
            val response = movieApi.searchMovies(query = query)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                val enrichedItems = coroutineScope {
                    body.results.map { movie ->
                        async {
                            SearchMovieItem(
                                movie = movie,
                                director = resolveDirector(movie)
                            )
                        }
                    }.awaitAll()
                }
                emit(Success(enrichedItems))
            } else {
                emit(Error(SEARCH_ERROR_MESSAGE))
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Timber.e(exception)
            emit(Error(SEARCH_ERROR_MESSAGE))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun resolveDirector(movie: Movie): String {
        directorCache[movie.id]?.let { return it }
        return try {
            val creditsResponse = movieApi.getCreditsMovie(movie.id)
            val creditsBody = creditsResponse.body()
            if (creditsResponse.isSuccessful && creditsBody != null) {
                val director = creditsBody.crew
                    .firstOrNull { it.job == DIRECTOR_JOB }
                    ?.name
                    .orEmpty()
                directorCache[movie.id] = director
                director
            } else {
                ""
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Timber.e(exception)
            ""
        }
    }
}
