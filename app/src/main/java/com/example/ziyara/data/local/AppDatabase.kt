package com.example.ziyara.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ziyara.data.local.AppDatabase.Place
import com.example.ziyara.data.local.AppDatabase.PlaceList
import com.example.ziyara.data.local.dao.PlaceDao
import com.example.ziyara.data.local.entity.PlaceEntity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                ).addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val places:List<Place> = readPlaces(context, "places.json")
                                    val entities = places.map { it.toEntity() }
                                    database.placeDao().insertPlaces(entities)
                                }
                            }
                        }
                    }
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

fun AppDatabase.Place.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        governorate = governorate,
        category = category,
        latitude = latitude,
        longitude = longitude,
        description = description,
        imageUrl = imageUrl
    )
}
//    read the data from the file
fun readPlaces(context: Context, filename: String): List<Place> {
    val jsonString = context.assets.open(filename).bufferedReader().use { it.readText() }
    val placeList = Gson().fromJson(jsonString, PlaceList::class.java)
    return placeList.places
}