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

    @Query("SELECT COUNT(*) FROM places")
    suspend fun getPlacesCount(): Int

    @Query("SELECT * FROM places WHERE is_favorite = 1")
    fun getFavoritePlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE id = :placeId")
    suspend fun getPlaceById(placeId: Int): PlaceEntity?

    @Query("SELECT * FROM places WHERE name LIKE '%' || :searchQuery || '%' OR governorate LIKE '%' || :searchQuery || '%'")
    fun searchPlaces(searchQuery: String): Flow<List<PlaceEntity>>

    @Query("UPDATE places SET is_favorite = :isFav WHERE id = :placeId")
    suspend fun updateFavoriteStatus(placeId: Int, isFav: Boolean)

    @Query("UPDATE places SET is_favorite = 0 WHERE is_favorite = 1")
    suspend fun clearAllFavorites()
}