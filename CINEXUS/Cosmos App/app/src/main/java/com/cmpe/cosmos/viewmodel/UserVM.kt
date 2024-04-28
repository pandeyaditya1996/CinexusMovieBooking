package com.cmpe.cosmos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmpe.cosmos.data.remote.models.Bookings
import com.cmpe.cosmos.data.remote.models.CancelResponse
import com.cmpe.cosmos.data.remote.models.MembershipResponse
import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.remote.models.RewardPoints
import com.cmpe.cosmos.data.repositories.UserRepository
import com.cmpe.cosmos.data.util.SafeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var movies = MutableStateFlow<MoviesWatchedState>(MoviesWatchedState.Loading)
        private set

    var bookings = MutableStateFlow<UserBookingsState>(UserBookingsState.Loading)
        private set

    var rewardPoints = MutableStateFlow<RewardPointsState>(RewardPointsState.Loading)
        private set

    var cancelBooking = MutableStateFlow<CancelBookingState>(CancelBookingState.Loading)
        private set

    var membership = MutableStateFlow<MembershipState>(MembershipState.Empty)
        private set

    fun getMoviesWatched(
        userId: Int,
    ) = viewModelScope.launch(Dispatchers.IO) {
        movies.value = MoviesWatchedState.Loading
        when (val result = userRepository.getMoviesWatched(userId)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    movies.value =
                        MoviesWatchedState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                movies.value = MoviesWatchedState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                movies.value = MoviesWatchedState.Error("Network Error")
            }
        }
    }

    fun getBookings(
        userId: Int,
    ) = viewModelScope.launch(Dispatchers.IO) {
        bookings.value = UserBookingsState.Loading
        when (val result = userRepository.getUserBookings(userId)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    bookings.value =
                        UserBookingsState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                bookings.value = UserBookingsState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                bookings.value = UserBookingsState.Error("Network Error")
            }
        }
    }

    fun getRewardPoints(
        userId: Int,
    ) = viewModelScope.launch(Dispatchers.IO) {
        rewardPoints.value = RewardPointsState.Loading
        when (val result = userRepository.getRewardPoints(userId)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    rewardPoints.value =
                        RewardPointsState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                rewardPoints.value = RewardPointsState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                rewardPoints.value = RewardPointsState.Error("Network Error")
            }
        }
    }

    fun cancelBooking(
        bookingId: Int,
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = userRepository.cancelTicket(bookingId)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    cancelBooking.value =
                        CancelBookingState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                cancelBooking.value = CancelBookingState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                cancelBooking.value = CancelBookingState.Error("Network Error")
            }
        }
    }

    fun postMembership(
        userId: Int,
        type: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        membership.value = MembershipState.Loading
        when (val result = userRepository.postMembership(userId, type)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    membership.value =
                        MembershipState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                membership.value = MembershipState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                membership.value = MembershipState.Error("Network Error")
            }
        }
    }

    sealed class MoviesWatchedState {
        object Loading : MoviesWatchedState()
        class Success(val response: List<MovieResponse>) : MoviesWatchedState()
        class Error(val message: String) : MoviesWatchedState()
    }

    sealed class UserBookingsState {
        object Loading : UserBookingsState()
        class Success(val response: List<Bookings>) : UserBookingsState()
        class Error(val message: String) : UserBookingsState()
    }

    sealed class RewardPointsState {
        object Loading : RewardPointsState()
        class Success(val response: RewardPoints) : RewardPointsState()
        class Error(val message: String) : RewardPointsState()
    }

    sealed class CancelBookingState {
        object Loading : CancelBookingState()
        class Success(val response: CancelResponse) : CancelBookingState()
        class Error(val message: String) : CancelBookingState()
    }

    sealed class MembershipState {
        object Loading : MembershipState()
        class Success(val response: MembershipResponse) : MembershipState()
        class Error(val message: String) : MembershipState()
        object Empty : MembershipState()
    }

}