package com.cmpe.cosmos.di

import com.cmpe.cosmos.data.remote.AuthService
import com.cmpe.cosmos.data.remote.BookingService
import com.cmpe.cosmos.data.remote.MovieService
import com.cmpe.cosmos.data.remote.UserService
import com.cmpe.cosmos.data.remote.client.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() = ApiClient.createHttpClient()

    @Provides
    @Singleton
    fun provideAuthService(client: OkHttpClient): AuthService =
        ApiClient.createAuthService(client)

    @Provides
    @Singleton
    fun provideMovieService(client: OkHttpClient): MovieService =
        ApiClient.createMovieService(client)

    @Provides
    @Singleton
    fun provideBookingService(client: OkHttpClient): BookingService =
        ApiClient.createBookingService(client)

    @Provides
    @Singleton
    fun provideUserService(client: OkHttpClient): UserService =
        ApiClient.createUserService(client)

}