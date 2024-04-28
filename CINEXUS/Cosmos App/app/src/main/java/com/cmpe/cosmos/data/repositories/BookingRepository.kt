package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.remote.models.CreateBookingResponse
import com.cmpe.cosmos.data.remote.models.SeatmapResponse
import com.cmpe.cosmos.data.util.SafeResult

interface BookingRepository {
    suspend fun getSeatmap(
        movieId: Int,
        theaterId: Int,
        showtime: String,
        date: String,
    ): SafeResult<SeatmapResponse>

    suspend fun createBooking(
        username: String,
        scheduleId: Int,
        totalAmount: Double,
        seatMapping: String,
        date: String,
        paymentType: String
    ): SafeResult<CreateBookingResponse>

}