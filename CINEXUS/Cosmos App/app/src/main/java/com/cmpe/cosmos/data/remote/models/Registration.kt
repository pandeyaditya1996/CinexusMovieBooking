package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName


data class RegistrationRequest(

    @SerializedName("username") var username: String? = null,
    @SerializedName("email_id") var email: String? = null,
    @SerializedName("password") var password: String? = null


)

data class RegistrationResponse(

    @SerializedName("error") var error: String? = null,
    @SerializedName("message") var message: String? = null

)