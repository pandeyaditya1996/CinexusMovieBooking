package com.cmpe.cosmos.di

import android.content.Context
import com.cmpe.cosmos.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase) = database.getMoviesDao()

    @Singleton
    @Provides
    fun provideTheatersDao(database: AppDatabase) = database.getTheatersDao()
}