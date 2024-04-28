package com.cmpe.cosmos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cmpe.cosmos.data.entities.Movies
import com.cmpe.cosmos.data.entities.Theaters
import com.cmpe.cosmos.data.local.dao.MoviesDao
import com.cmpe.cosmos.data.local.dao.TheatersDao

@Database(entities = [Movies::class, Theaters::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMoviesDao(): MoviesDao

    abstract fun getTheatersDao(): TheatersDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}