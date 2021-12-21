package com.exaper.launcher.api

import com.exaper.launcher.api.data.DenyPolicy
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class PolicyApiClient @Inject constructor(private val apiService: PolicyApiService) {
    suspend fun getDenyList() = callSafely { apiService.getDenyList() }.map { it.denylist.map { packageName -> DenyPolicy(packageName) } }

    @Suppress("TooGenericExceptionCaught") // We have no type information about what's bubbling up from Moshi and Retrofit
    private suspend fun <T> callSafely(block: suspend () -> Response<T>): Result<T> {
        val response = try {
            block()
        } catch (e: HttpException) {
            return Result.failure(e)
        } catch (e: Exception) {
            return Result.failure(e)
        }

        val body = response.body()

        return if (response.isSuccessful && body != null) {
            Result.success(body)
        } else {
            Result.failure(IOException("Could not execute request"))
        }
    }
}