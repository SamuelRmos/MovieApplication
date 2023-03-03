package com.example.retrofitkotlin.repository

import com.example.commons.functional.Either
import com.example.commons.functional.Either.Error
import com.example.commons.functional.Either.Success
import com.example.commons.service.ConnectionService
import retrofit2.Response

open class BaseRepository(private val connectionService: ConnectionService) {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Either<String, T> {

        if (connectionService.isNetworkAvailable().not()) {
            return Error("Device not connected")
        }

        call.invoke().also {
            return if (it.isSuccessful) {
                Success(it.body()!!)
            } else {
                Error("Error fetching movies data")
            }
        }
    }
}