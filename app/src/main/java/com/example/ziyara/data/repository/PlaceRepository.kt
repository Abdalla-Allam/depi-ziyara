package com.example.ziyara.data.repository

import android.content.Context
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

    suspend fun clearAllFavorites() {
        placeDao.clearAllFavorites()
    }

    suspend fun prepopulateDatabase(context: Context) {
        val currentCount = placeDao.getPlacesCount()
        if (currentCount == 0) {
            val jsonString = context.assets.open("places.json").bufferedReader().use { it.readText() }
            val placeListarray = Gson().fromJson(jsonString, Array<Place>::class.java)

            val entities = placeListarray.map { place ->
                val categoryLower = place.category.lowercase().trim()
                val nameLower = place.name.lowercase()

                val calculatedPrice = when {
                    categoryLower.contains("market") || categoryLower.contains("nature") ||
                            categoryLower.contains("park") || categoryLower.contains("oasis") ||
                            nameLower.contains("khan el") -> 0.0

                    categoryLower.contains("museum") || categoryLower.contains("temple") ||
                            categoryLower.contains("pyramid") || nameLower.contains("museum") ||
                            nameLower.contains("pyramids") -> listOf(40.0, 60.0, 80.0, 100.0).random()

                    else -> listOf(30.0, 50.0, 60.0).random()
                }

                PlaceEntity(
                    id = place.id,
                    name = place.name,
                    governorate = place.governorate,
                    category = place.category,
                    latitude = place.latitude,
                    longitude = place.longitude,
                    description = place.description,
                    imageUrl = place.imageUrl,
                    ticketPrice = calculatedPrice,
                    isFavorite = false
                )
            }
            placeDao.insertPlaces(entities)
        }
    }
}