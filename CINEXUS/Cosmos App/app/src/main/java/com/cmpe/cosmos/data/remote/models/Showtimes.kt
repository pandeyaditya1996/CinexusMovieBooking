package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class Showtimes(

    @SerializedName("showtime") var showtime: String? = null,
    @SerializedName("discount") var discount: Int? = null,
    @SerializedName("from_date") var fromDate: String? = null,
    @SerializedName("to_date") var toDate: String? = null

)
