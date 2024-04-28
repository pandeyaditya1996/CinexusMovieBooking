package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class Bookings(

    @SerializedName("booking_id") var bookingId: Int? = null,
    @SerializedName("total_amount_paid") var totalAmountPaid: Double? = null,
    @SerializedName("booking_timestamp") var bookingTimestamp: String? = null,
    @SerializedName("show_timestamp") var showTimestamp: String? = null,
    @SerializedName("is_refund_requested") var isRefundRequested: Boolean? = null,
    @SerializedName("points_earned") var pointsEarned: Int? = null,
    @SerializedName("seat_list") var seatList: String? = null,
    @SerializedName("movies") var movies: ArrayList<MovieItem> = arrayListOf()

)

data class MovieItem(

    @SerializedName("title") var title: String? = null,
    @SerializedName("poster_url") var posterUrl: String? = null

)