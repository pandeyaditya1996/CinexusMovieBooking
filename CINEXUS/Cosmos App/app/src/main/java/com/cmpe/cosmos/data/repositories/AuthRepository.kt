package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.remote.models.LoginResponse
import com.cmpe.cosmos.data.remote.models.RegistrationResponse
import com.cmpe.cosmos.data.util.SafeResult


interface AuthRepository {
    suspend fun postLogin(username: String, password: String): SafeResult<LoginResponse>

    suspend fun postRegistration(
        username: String,
        email: String,
        password: String
    ): SafeResult<RegistrationResponse>
}