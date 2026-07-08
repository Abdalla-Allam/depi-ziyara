package com.example.ziyara.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ziyara.data.local.dao.PlaceDao
import com.example.ziyara.data.local.entity.PlaceEntity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    data class Place(
        val id: Int,
        val name: String,
        val governorate: String,
        val category: String,
        val latitude: Double,
        val longitude: Double,
        val imageUrl: String,
        val description: String
    )

    data class PlaceList(val places: List<Place>)

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ziyara_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val jsonString = context.assets.open("places.json").bufferedReader().use { it.readText() }
                        val placeList = Gson().fromJson(jsonString, PlaceList::class.java)
                        val entities = placeList?.places?.map {
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
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}