package com.cmpe.cosmos.di

import com.cmpe.cosmos.data.repositories.AuthRepository
import com.cmpe.cosmos.data.repositories.AuthRepositoryImpl
import com.cmpe.cosmos.data.repositories.BookingRepository
import com.cmpe.cosmos.data.repositories.BookingRepositoryImpl
import com.cmpe.cosmos.data.repositories.MovieRepository
import com.cmpe.cosmos.data.repositories.MovieRepositoryImpl
import com.cmpe.cosmos.data.repositories.TheaterRepository
import com.cmpe.cosmos.data.repositories.TheaterRepositoryImpl
import com.cmpe.cosmos.data.repositories.UserRepository
import com.cmpe.cosmos.data.repositories.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindTheaterRepository(
        theaterRepositoryImpl: TheaterRepositoryImpl
    ): TheaterRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}