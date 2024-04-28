package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.remote.models.Bookings
import com.cmpe.cosmos.data.remote.models.CancelResponse
import com.cmpe.cosmos.data.remote.models.MembershipResponse
import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.remote.models.RewardPoints
import com.cmpe.cosmos.data.util.SafeResult

interface UserRepository {
    suspend fun getMoviesWatched(userId: Int): SafeResult<List<MovieResponse>>

    suspend fun getUserBookings(userId: Int): SafeResult<List<Bookings>>

    suspend fun getRewardPoints(
        userId: Int
    ): SafeResult<RewardPoints>

    suspend fun cancelTicket(
        bookingId: Int
    ): SafeResult<CancelResponse>

    suspend fun postMembership(
        userId: Int,
        type: String
    ): SafeResult<MembershipResponse>


}