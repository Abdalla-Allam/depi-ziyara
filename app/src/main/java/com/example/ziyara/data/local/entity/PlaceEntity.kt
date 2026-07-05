package com.example.ziyara.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String = "",
    val governorate: String,
    val category: String,
    val ticketPrice: String = "60 EGP",
    val isFavorite: Boolean = false
)