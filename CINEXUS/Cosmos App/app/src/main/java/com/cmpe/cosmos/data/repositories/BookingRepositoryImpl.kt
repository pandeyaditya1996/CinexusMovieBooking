package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.remote.models.CreateBookingResponse
import com.cmpe.cosmos.data.remote.models.SeatmapResponse
import com.cmpe.cosmos.data.sources.BookingDataSource
import com.cmpe.cosmos.data.util.SafeResult
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val remoteDataSource: BookingDataSource,
) : BookingRepository {
    override suspend fun getSeatmap(
        movieId: Int,
        theaterId: Int,
        showtime: String,
        date: String
    ): SafeResult<SeatmapResponse> {
        return when (val result = remoteDataSource.getSeatmap(movieId, theaterId, showtime, date)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
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
        return when (val result = remoteDataSource.createBooking(
            username = username,
            scheduleId = scheduleId,
            totalAmount = totalAmount,
            seatMapping = seatMapping,
            date = date,
            paymentType = paymentType
        )
        ) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }
}