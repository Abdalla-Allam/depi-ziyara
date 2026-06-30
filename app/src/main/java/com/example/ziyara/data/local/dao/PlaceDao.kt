package com.example.ziyara.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ziyara.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)


    @Query("SELECT * FROM places")
    fun getAllPlaces(): Flow<List<PlaceEntity>>


    @Query("SELECT * FROM places WHERE isFavorite = 1")
    fun getFavoritePlaces(): Flow<List<PlaceEntity>>


    @Query("SELECT * FROM places WHERE name LIKE :searchQuery OR city = :city")
    fun searchAndFilterPlaces(searchQuery: String, city: String): Flow<List<PlaceEntity>>


    @Query("UPDATE places SET isFavorite = :isFav WHERE id = :placeId")
    suspend fun updateFavoriteStatus(placeId: Int, isFav: Boolean)
}