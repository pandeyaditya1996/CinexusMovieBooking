package com.cmpe.cosmos.data.sources

import com.cmpe.cosmos.data.remote.BookingService
import com.cmpe.cosmos.data.remote.models.CreateBookingRequest
import com.cmpe.cosmos.data.remote.models.CreateBookingResponse
import com.cmpe.cosmos.data.remote.models.SeatmapRequest
import com.cmpe.cosmos.data.remote.models.SeatmapResponse
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.data.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class BookingDataSourceImpl(
    private val bookingService: BookingService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookingDataSource {
    override suspend fun getSeatmap(
        movieId: Int,
        theaterId: Int,
        showtime: String,
        date: String
    ): SafeResult<SeatmapResponse> {
        return safeApiCall(dispatcher) {
            bookingService.getSeatmap(
                SeatmapRequest(
                    movieId = movieId,
                    theaterId = theaterId,
                    showtime = showtime,
                    date = date
                )
            )
        }
    }

    override suspend fun createBooking(
        username: String,
        scheduleId: Int,
        totalAmount: Double,
        seatMapping: String,
        date: String,
        paymentType: String
    ): SafeResult<CreateBookingResponse> {
        return safeApiCall(dispatcher) {
            bookingService.createBooking(
                username = username,
                CreateBookingRequest(
                    scheduleId = scheduleId,
                    totalAmount = totalAmount,
                    seatMapping = seatMapping,
                    date = date,
                    paymentType = paymentType
                )
            )
        }
    }
}