package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteVideoEntity::class, HistoryVideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TrendyDatabase : RoomDatabase() {

    abstract fun trendyDao(): TrendyDao

    companion object {
        @Volatile
        private var instance: TrendyDatabase? = null

        fun getInstance(context: Context): TrendyDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TrendyDatabase::class.java,
                    "trendy_videos.db"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
        }
    }
}
