package com.cmpe.cosmos.di

import com.cmpe.cosmos.data.remote.AuthService
import com.cmpe.cosmos.data.remote.BookingService
import com.cmpe.cosmos.data.remote.MovieService
import com.cmpe.cosmos.data.remote.UserService
import com.cmpe.cosmos.data.sources.AuthDataSource
import com.cmpe.cosmos.data.sources.AuthDataSourceImpl
import com.cmpe.cosmos.data.sources.BookingDataSource
import com.cmpe.cosmos.data.sources.BookingDataSourceImpl
import com.cmpe.cosmos.data.sources.MovieDataSource
import com.cmpe.cosmos.data.sources.MovieDataSourceImpl
import com.cmpe.cosmos.data.sources.TheaterDataSource
import com.cmpe.cosmos.data.sources.TheaterDataSourceImpl
import com.cmpe.cosmos.data.sources.UserDataSource
import com.cmpe.cosmos.data.sources.UserDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SourcesModule {

    @Provides
    @Singleton
    fun provideAuthDataSource(apiService: AuthService): AuthDataSource {
        return AuthDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(apiService: MovieService): MovieDataSource {
        return MovieDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideTheaterDataSource(apiService: MovieService): TheaterDataSource {
        return TheaterDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideBookingDataSource(apiService: BookingService): BookingDataSource {
        return BookingDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideUserDataSource(apiService: UserService): UserDataSource {
        return UserDataSourceImpl(apiService)
    }
}