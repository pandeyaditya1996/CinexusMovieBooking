package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class CancelResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("error") var error: String? = null
)