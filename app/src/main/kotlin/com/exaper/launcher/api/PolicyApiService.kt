package com.exaper.launcher.api

import com.exaper.launcher.api.data.DenyListResponse
import retrofit2.Response
import retrofit2.http.GET

interface PolicyApiService {
    @GET("/b/61575fdf4a82881d6c5923a5")
    suspend fun getDenyList(): Response<DenyListResponse>
}