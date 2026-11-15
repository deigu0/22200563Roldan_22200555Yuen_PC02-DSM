package com.esan.diarioespacial.data.api

import com.google.gson.annotations.SerializedName

data class NeoWsFeedResponseDto(
    @SerializedName("element_count")
    val elementCount: Int? = null,
    @SerializedName("near_earth_objects")
    val nearEarthObjects: Map<String, List<NeoWsAsteroidDto>>? = null
)

data class NeoWsAsteroidDto(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean? = null,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: EstimatedDiameterDto? = null,
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproachDataDto>? = null
)

data class EstimatedDiameterDto(
    @SerializedName("meters")
    val meters: MetersDiameterDto? = null
)

data class MetersDiameterDto(
    @SerializedName("estimated_diameter_min")
    val minMeters: Double? = null,
    @SerializedName("estimated_diameter_max")
    val maxMeters: Double? = null
)

data class CloseApproachDataDto(
    @SerializedName("close_approach_date")
    val closeApproachDate: String? = null,
    @SerializedName("relative_velocity")
    val relativeVelocity: RelativeVelocityDto? = null,
    @SerializedName("miss_distance")
    val missDistance: MissDistanceDto? = null
)

data class RelativeVelocityDto(
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: String? = null
)

data class MissDistanceDto(
    @SerializedName("kilometers")
    val kilometers: String? = null
)
