package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class MembershipResponse(
    @SerializedName("message") var message: String? = null
)

data class MembershipRequest(

    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("membership_type") var membershipType: String? = null

)