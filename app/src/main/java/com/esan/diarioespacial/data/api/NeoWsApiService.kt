package com.esan.diarioespacial.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NeoWsApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidFeed(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): NeoWsFeedResponseDto

    @GET("neo/rest/v1/neo/{id}")
    suspend fun getAsteroidDetail(
        @Path("id") id: String,
        @Query("api_key") apiKey: String
    ): NeoWsAsteroidDto
}
