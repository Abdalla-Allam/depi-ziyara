package com.example.ziyara.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",

    @ColumnInfo(name = "governorate")
    val governorate: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "ticket_price")
    val ticketPrice: Double = 60.0,

    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)