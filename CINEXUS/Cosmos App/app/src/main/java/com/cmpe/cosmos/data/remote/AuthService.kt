package com.cmpe.cosmos.data.remote

import com.cmpe.cosmos.data.constant.ApiEndpoints.ENDPOINT_LOGIN
import com.cmpe.cosmos.data.constant.ApiEndpoints.ENDPOINT_REGISTER
import com.cmpe.cosmos.data.remote.models.LoginRequest
import com.cmpe.cosmos.data.remote.models.LoginResponse
import com.cmpe.cosmos.data.remote.models.RegistrationRequest
import com.cmpe.cosmos.data.remote.models.RegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(ENDPOINT_LOGIN)
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST(ENDPOINT_REGISTER)
    suspend fun registration(@Body registrationRequest: RegistrationRequest): RegistrationResponse

}