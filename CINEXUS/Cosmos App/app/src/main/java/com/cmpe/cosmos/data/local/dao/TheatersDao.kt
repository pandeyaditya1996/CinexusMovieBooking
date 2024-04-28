package com.cmpe.cosmos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cmpe.cosmos.data.entities.Theaters
import kotlinx.coroutines.flow.Flow

@Dao
interface TheatersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTheaters(theaters: List<Theaters>)

    @Query("SELECT * FROM theaters WHERE locationId = :location")
    fun getTheaters(location: Int): Flow<List<Theaters>>
}