package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.remote.models.LoginResponse
import com.cmpe.cosmos.data.remote.models.RegistrationResponse
import com.cmpe.cosmos.data.sources.AuthDataSource
import com.cmpe.cosmos.data.util.SafeResult
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun postLogin(username: String, password: String): SafeResult<LoginResponse> {
        return when (val result = remoteDataSource.postLogin(username, password)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun postRegistration(
        username: String,
        email: String,
        password: String
    ): SafeResult<RegistrationResponse> {
        return when (val result = remoteDataSource.postRegistration(username, email, password)) {
            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

}
