package com.example.ziyara.data.local

import android.content.Context
import android.util.Log
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

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    // الـ POJO اللي بنقرا بيه الـ JSON
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

                // 💡 الحل السحري: بنخليه ينادي على الداتابيز في الخلفية فوراً عشان يجبر الـ onCreate تشتغل
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        instance.placeDao().getAllPlaces() // أو أي دالة Query عندك جوه الـ Dao لتهيئة قاعدة البيانات
                    } catch (e: Exception) {
                        Log.e("ZiyaraDebug", "Warmup failed", e)
                    }
                }

                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // 1. قراءة ملف الـ JSON
                        val jsonString = context.assets.open("places.json").bufferedReader().use { it.readText() }
                        val placeListarray = Gson().fromJson(jsonString, Array<Place>::class.java)

                        // 2. تحويل البيانات لـ PlaceEntity
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

                        // 3. الحل الهندسي: بنعمل Insert مباشرة جوه الـ db اللي متاح معانا في الـ onCreate
                        // من غير ما ننادي على getDatabase() ونعمل Deadlock!
                        if (entities.isNotEmpty()) {
                            db.beginTransaction()
                            try {
                                entities.forEach { place ->
                                    db.execSQL(
                                        """
                                        INSERT OR IGNORE INTO places (id, name, governorate, category, latitude, longitude, description, imageUrl, ticketPrice, isFavorite) 
                                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                                        """.trimIndent(),
                                        arrayOf(
                                            place.id, place.name, place.governorate, place.category,
                                            place.latitude, place.longitude, place.description,
                                            place.imageUrl, place.ticketPrice, if (place.isFavorite) 1 else 0
                                        )
                                    )
                                }
                                db.setTransactionSuccessful()
                                Log.d("ZiyaraDebug", "Prepopulation successfully inserted 14 places!")
                            } finally {
                                db.endTransaction()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ZiyaraDebug", "Prepopulation failed!", e)
                    }
                }
            }
        }
    }
}