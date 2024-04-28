package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class TheaterResponse(
    @SerializedName("theater_id") var theaterId: Int? = null,
    @SerializedName("location_id") var locationId: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("seating_capacity") var seatingCapacity: Int? = null,
    @SerializedName("distance") var distance: String? = null,
    @SerializedName("coordinates") var coordinates: String? = null,
    @SerializedName("address") var address: String? = null
)