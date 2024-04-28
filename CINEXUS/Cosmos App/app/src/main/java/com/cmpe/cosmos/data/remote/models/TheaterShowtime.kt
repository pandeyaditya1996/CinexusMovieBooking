package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName


data class TheaterShowtimeRequest(

    @SerializedName("movie_id") var movieId: String? = null,
    @SerializedName("location_id") var locationId: String? = null

)

data class TheaterShowtimeResponse(

    @SerializedName("theater_id") var theaterId: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("seating_capacity") var seatingCapacity: Int? = null,
    @SerializedName("distance") var distance: String? = null,
    @SerializedName("coordinates") var coordinates: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("showtimes") var showtimes: ArrayList<Showtimes> = arrayListOf()

)