package com.cmpe.cosmos.data.sources

import com.cmpe.cosmos.data.remote.UserService
import com.cmpe.cosmos.data.remote.models.Bookings
import com.cmpe.cosmos.data.remote.models.CancelResponse
import com.cmpe.cosmos.data.remote.models.MembershipRequest
import com.cmpe.cosmos.data.remote.models.MembershipResponse
import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.remote.models.RewardPoints
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.data.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val service: UserService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {
    override suspend fun getMoviesWatched(userId: Int): SafeResult<List<MovieResponse>> {
        return safeApiCall(dispatcher) {
            service.getMoviesWatched(userId)
        }
    }

    override suspend fun getUserBookings(userId: Int): SafeResult<List<Bookings>> {
        return safeApiCall(dispatcher) {
            service.getUserBookings(userId)
        }
    }

    override suspend fun getRewardPoints(userId: Int): SafeResult<RewardPoints> {
        return safeApiCall(dispatcher) {
            service.getRewardPoints(userId)
        }
    }

    override suspend fun cancelTicket(bookingId: Int): SafeResult<CancelResponse> {
        return safeApiCall(dispatcher) {
            service.cancelBookings(bookingId)
        }
    }

    override suspend fun postMembership(userId: Int, type: String): SafeResult<MembershipResponse> {
        return safeApiCall(dispatcher) {
            service.postMembership(MembershipRequest(userId, type))
        }
    }
}