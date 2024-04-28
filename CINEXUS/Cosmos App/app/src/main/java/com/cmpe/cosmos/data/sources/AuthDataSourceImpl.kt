package com.cmpe.cosmos.data.sources

import com.cmpe.cosmos.data.remote.AuthService
import com.cmpe.cosmos.data.remote.models.LoginRequest
import com.cmpe.cosmos.data.remote.models.LoginResponse
import com.cmpe.cosmos.data.remote.models.RegistrationRequest
import com.cmpe.cosmos.data.remote.models.RegistrationResponse
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.data.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AuthDataSourceImpl(
    private val authService: AuthService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthDataSource {
    override suspend fun postLogin(
        username: String,
        password: String
    ): SafeResult<LoginResponse> {
        return safeApiCall(dispatcher) {
            authService.login(LoginRequest(username = username, password = password))
        }
    }

    override suspend fun postRegistration(
        username: String,
        email: String,
        password: String
    ): SafeResult<RegistrationResponse> {
        return safeApiCall(dispatcher) {
            authService.registration(
                RegistrationRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
        }
    }
}