package com.cmpe.cosmos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "theaters")
data class Theaters(
    @PrimaryKey
    val theaterId: Int,
    val locationId: Int,
    val name: String,
    val seatingCapacity: Int,
    val distance: String,
    val coordinates: String,
    val address: String,
)