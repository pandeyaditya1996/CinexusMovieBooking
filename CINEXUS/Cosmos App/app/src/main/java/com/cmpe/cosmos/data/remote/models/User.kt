package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("user_role") var userRole: String? = null,
    @SerializedName("membership_type") var membershipType: String? = null

)
