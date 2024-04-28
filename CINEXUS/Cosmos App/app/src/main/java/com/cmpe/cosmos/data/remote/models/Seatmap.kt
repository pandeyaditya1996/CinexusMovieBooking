package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class SeatmapRequest(

    @SerializedName("movie_id") var movieId: Int? = null,
    @SerializedName("theater_id") var theaterId: Int? = null,
    @SerializedName("showtime") var showtime: String? = null,
    @SerializedName("date") var date: String? = null

)

data class SeatmapResponse(
    @SerializedName("current_seatmap") var currentSeatmap: String? = null,
    @SerializedName("schedule_id") var scheduleId: Int? = null,
)