package com.cmpe.cosmos.model

data class TicketModel(

    var bookingId: Int? = null,
    var scheduleId: Int? = null,
    var totalAmountPaid: Double? = null,
    var bookingTimestamp: String? = null,
    var showTimeStamp: String? = null,
    var isRefundRequested: Boolean? = null,
    var pointsEarned: Int? = null,
    var seatList: String? = null,
    var title: String? = null,
    var posterUrl: String? = null
)