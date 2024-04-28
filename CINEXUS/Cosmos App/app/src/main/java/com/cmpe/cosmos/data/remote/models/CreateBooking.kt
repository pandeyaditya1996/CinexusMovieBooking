package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateBookingRequest(

    @SerializedName("schedule_id") var scheduleId: Int? = null,
    @SerializedName("total_amount") var totalAmount: Double? = null,
    @SerializedName("seat_mapping") var seatMapping: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("payment_type") var paymentType: String? = null

)

data class CreateBookingResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("booking_id") var bookingId: Int? = null,
)