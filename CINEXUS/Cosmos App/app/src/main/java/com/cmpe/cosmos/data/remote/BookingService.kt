package com.cmpe.cosmos.data.remote

import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.data.remote.models.CreateBookingRequest
import com.cmpe.cosmos.data.remote.models.CreateBookingResponse
import com.cmpe.cosmos.data.remote.models.SeatmapRequest
import com.cmpe.cosmos.data.remote.models.SeatmapResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface BookingService {

    @POST(ApiEndpoints.ENDPOINT_SEATMAP_BY_ID)
    suspend fun getSeatmap(@Body seatmapRequest: SeatmapRequest): SeatmapResponse

    @POST(ApiEndpoints.ENDPOINT_CREATE_BOOKING)
    suspend fun createBooking(
        @Query("username") username: String,
        @Body createBookingRequest: CreateBookingRequest
    ): CreateBookingResponse


}