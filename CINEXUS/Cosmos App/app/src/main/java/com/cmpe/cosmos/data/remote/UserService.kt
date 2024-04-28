package com.cmpe.cosmos.data.remote

import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.data.remote.models.Bookings
import com.cmpe.cosmos.data.remote.models.CancelResponse
import com.cmpe.cosmos.data.remote.models.MembershipRequest
import com.cmpe.cosmos.data.remote.models.MembershipResponse
import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.remote.models.RewardPoints
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
    @GET("${ApiEndpoints.ENDPOINT_USER}{USER_ID}/${ApiEndpoints.ENDPOINT_MOVIES_WATCHED}")
    suspend fun getMoviesWatched(@Path("USER_ID") userId: Int): List<MovieResponse>

    @GET("${ApiEndpoints.ENDPOINT_USER_BOOKINGS}{USER_ID}/")
    suspend fun getUserBookings(@Path("USER_ID") userId: Int): List<Bookings>

    @GET("${ApiEndpoints.ENDPOINT_CANCEL_BOOKING}{BOOKING_ID}/")
    suspend fun cancelBookings(@Path("BOOKING_ID") bookingId: Int): CancelResponse

    @GET("${ApiEndpoints.ENDPOINT_REWARD_POINTS}{USER_ID}/")
    suspend fun getRewardPoints(@Path("USER_ID") userId: Int): RewardPoints

    @POST(ApiEndpoints.ENDPOINT_UPDATE_MEMBERSHIP)
    suspend fun postMembership(@Body request: MembershipRequest): MembershipResponse
}