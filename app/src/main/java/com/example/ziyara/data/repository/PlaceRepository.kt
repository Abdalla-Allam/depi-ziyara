package com.example.ziyara.data.repository

import com.example.ziyara.data.local.dao.PlaceDao
import com.example.ziyara.data.local.entity.PlaceEntity
import kotlinx.coroutines.flow.Flow

class PlaceRepository(private val placeDao: PlaceDao) {

    fun getAllPlaces(): Flow<List<PlaceEntity>> = placeDao.getAllPlaces()

    fun getFavoritePlaces(): Flow<List<PlaceEntity>> = placeDao.getFavoritePlaces()

    fun searchPlaces(query: String): Flow<List<PlaceEntity>> = placeDao.searchPlaces(query)

    suspend fun getPlaceById(placeId: Int): PlaceEntity? = placeDao.getPlaceById(placeId)

    suspend fun toggleFavorite(placeId: Int, isFavorite: Boolean) {
        placeDao.updateFavoriteStatus(placeId, isFavorite)
    }
}