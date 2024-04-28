package com.cmpe.cosmos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmpe.cosmos.data.remote.models.CreateBookingResponse
import com.cmpe.cosmos.data.remote.models.SeatmapResponse
import com.cmpe.cosmos.data.repositories.BookingRepository
import com.cmpe.cosmos.data.util.SafeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingVM @Inject constructor(
    private val bookingRepository: BookingRepository,
) : ViewModel() {
    var seatmap = MutableStateFlow<SeatmapState>(SeatmapState.Loading)
        private set

    var booking = MutableStateFlow<CreateBookingState>(CreateBookingState.Empty)
        private set

    fun getSeatmap(
        movieId: Int,
        theaterId: Int,
        showtime: String,
        date: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        seatmap.value = SeatmapState.Loading
        when (val result = bookingRepository.getSeatmap(movieId, theaterId, showtime, date)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    seatmap.value =
                        SeatmapState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                seatmap.value = SeatmapState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                seatmap.value = SeatmapState.Error("Network Error")
            }
        }
    }

    fun createBooking(
        username: String,
        scheduleId: Int,
        totalAmount: Double,
        seatMapping: String,
        date: String,
        paymentType: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (val result =
            bookingRepository.createBooking(
                username,
                scheduleId,
                totalAmount,
                seatMapping,
                date,
                paymentType
            )) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    booking.value =
                        CreateBookingState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                booking.value = CreateBookingState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                booking.value = CreateBookingState.Error("Network Error")
            }
        }
    }

    sealed class SeatmapState {
        object Loading : SeatmapState()
        class Success(val response: SeatmapResponse) : SeatmapState()
        class Error(val message: String) : SeatmapState()
    }

    sealed class CreateBookingState {
        object Empty : CreateBookingState()
        object Loading : CreateBookingState()
        class Success(val response: CreateBookingResponse) : CreateBookingState()
        class Error(val message: String) : CreateBookingState()
    }
}