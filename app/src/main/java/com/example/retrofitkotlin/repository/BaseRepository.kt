package com.example.retrofitkotlin.repository

import com.example.commons.functional.Either
import com.example.commons.service.ConnectionService
import retrofit2.Response

open class BaseRepository(private val connectionService: ConnectionService) {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Either<String, T> {

        if (connectionService.isNetworkAvailable().not()) {
            return Either.Error("Device not connected")
        }

        with(call.invoke()) {
            return if (isSuccessful) {
                Either.Success(body()!!)
            } else {
                Either.Error("Error fetching movies data")
            }
        }
    }
}