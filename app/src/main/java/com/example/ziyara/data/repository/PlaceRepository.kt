package com.example.ziyara.data.repository

import android.content.Context
import com.example.ziyara.data.local.AppDatabase.Companion.getDatabase
import com.example.ziyara.data.local.AppDatabase.Place
import com.example.ziyara.data.local.dao.PlaceDao
import com.example.ziyara.data.local.entity.PlaceEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class PlaceRepository(private val placeDao: PlaceDao) {

    fun getAllPlaces(): Flow<List<PlaceEntity>> = placeDao.getAllPlaces()

    fun getFavoritePlaces(): Flow<List<PlaceEntity>> = placeDao.getFavoritePlaces()

    fun searchPlaces(query: String): Flow<List<PlaceEntity>> = placeDao.searchPlaces(query)

    suspend fun getPlaceById(placeId: Int): PlaceEntity? = placeDao.getPlaceById(placeId)

    suspend fun toggleFavorite(placeId: Int, isFavorite: Boolean) {
        placeDao.updateFavoriteStatus(placeId, isFavorite)
    }
    suspend fun pepopulateDatabase(context: Context){
        val currentcount=placeDao.getCount()
        if(currentcount==0){
            val jsonString = context.assets.open("places.json").bufferedReader().use { it.readText() }
            val placeListarray = Gson().fromJson(jsonString, Array<Place>::class.java)
            val entities = placeListarray?.map {
                PlaceEntity(
                    id = it.id,
                    name = it.name,
                    governorate = it.governorate,
                    category = it.category,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    ticketPrice = "Free",
                    isFavorite = false
                )
            } ?: emptyList()

            getDatabase(context).placeDao().insertPlaces(entities)
        }
    }
}