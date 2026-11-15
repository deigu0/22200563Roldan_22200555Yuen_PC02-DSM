package com.esan.diarioespacial.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApodApiService {
    @GET("wp-json/wp/v2/apod-basic")
    suspend fun getApodList(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<ApodDto>
}
