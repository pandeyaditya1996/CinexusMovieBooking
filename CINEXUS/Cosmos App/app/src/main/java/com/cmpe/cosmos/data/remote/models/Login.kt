package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName


data class LoginRequest(

    @SerializedName("username") var username: String? = null,
    @SerializedName("password") var password: String? = null

)

data class LoginResponse(

    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("user_membership") var membership: String? = null,
    @SerializedName("total_points_earned") var reward: Int? = null

)