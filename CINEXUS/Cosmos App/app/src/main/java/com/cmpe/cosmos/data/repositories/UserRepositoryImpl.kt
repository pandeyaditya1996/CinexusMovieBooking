package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.remote.models.Bookings
import com.cmpe.cosmos.data.remote.models.CancelResponse
import com.cmpe.cosmos.data.remote.models.MembershipResponse
import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.remote.models.RewardPoints
import com.cmpe.cosmos.data.sources.UserDataSource
import com.cmpe.cosmos.data.util.SafeResult
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserDataSource,
) : UserRepository {
    override suspend fun getMoviesWatched(userId: Int): SafeResult<List<MovieResponse>> {
        return when (val result = remoteDataSource.getMoviesWatched(userId)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun getUserBookings(userId: Int): SafeResult<List<Bookings>> {
        return when (val result = remoteDataSource.getUserBookings(userId)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun getRewardPoints(userId: Int): SafeResult<RewardPoints> {
        return when (val result = remoteDataSource.getRewardPoints(userId)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun cancelTicket(bookingId: Int): SafeResult<CancelResponse> {
        return when (val result = remoteDataSource.cancelTicket(bookingId)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun postMembership(userId: Int, type: String): SafeResult<MembershipResponse> {
        return when (val result = remoteDataSource.postMembership(userId, type)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }
}