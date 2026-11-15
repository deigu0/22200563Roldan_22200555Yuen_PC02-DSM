package com.esan.diarioespacial.data.api

import com.google.gson.annotations.SerializedName

data class ApodDto(
    @SerializedName("date") val date: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("hdurl") val hdurl: String?,
    @SerializedName("credit") val credit: String?
)
