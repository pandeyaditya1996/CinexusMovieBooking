package com.cmpe.cosmos.data.remote.client

import com.cmpe.cosmos.data.constant.ApiEndpoints.BASE_URL
import com.cmpe.cosmos.data.remote.AuthService
import com.cmpe.cosmos.data.remote.BookingService
import com.cmpe.cosmos.data.remote.MovieService
import com.cmpe.cosmos.data.remote.UserService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    fun createHttpClient(): OkHttpClient {
        val requestInterceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .build()
            return@Interceptor chain.proceed(request)
        }
        val httpClient =
            OkHttpClient.Builder().addInterceptor(requestInterceptor).addNetworkInterceptor(
                interceptor
            )
        return httpClient.build()
    }

    fun createAuthService(
        client: OkHttpClient
    ): AuthService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    fun createMovieService(
        client: OkHttpClient
    ): MovieService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieService::class.java)
    }

    fun createBookingService(
        client: OkHttpClient
    ): BookingService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookingService::class.java)
    }

    fun createUserService(
        client: OkHttpClient
    ): UserService {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}