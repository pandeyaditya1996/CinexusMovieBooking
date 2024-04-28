package com.cmpe.cosmos.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmpe.cosmos.data.remote.models.LoginResponse
import com.cmpe.cosmos.data.remote.models.RegistrationResponse
import com.cmpe.cosmos.data.repositories.AuthRepository
import com.cmpe.cosmos.data.util.SafeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var login = MutableStateFlow<State>(State.Empty)
        private set

    var register = MutableStateFlow<State>(State.Empty)
        private set

    fun doLogin(
        username: String,
        password: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        login.value = State.Loading
        when (val result = authRepository.postLogin(username, password)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    login.value =
                        State.LoginSuccess(safeResult)
                }
            }

            is SafeResult.Failure -> {
                login.value = State.Error(result.message)
            }

            SafeResult.NetworkError -> {
                login.value = State.Error("Network Error")
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        register.value = State.Loading
        when (val result = authRepository.postRegistration(username, email, password)) {
            is SafeResult.Success -> {
                Log.d("test", "Success")
                result.data.let { safeResult ->
                    register.value =
                        State.RegistrationSuccess(safeResult)
                }
            }

            is SafeResult.Failure -> {
                Log.d("test", "Failure")
                register.value = State.Error(result.message)
            }

            SafeResult.NetworkError -> {
                Log.d("test", "NetworkError")
                register.value = State.Error("Network Error")
            }
        }
    }

    sealed class State {
        object Empty : State()
        object Loading : State()
        class LoginSuccess(val response: LoginResponse) : State()
        class RegistrationSuccess(val response: RegistrationResponse) : State()
        class Error(val message: String) : State()
    }

}