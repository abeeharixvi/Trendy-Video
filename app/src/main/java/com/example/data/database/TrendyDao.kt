package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrendyDao {

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteVideoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE videoId = :videoId)")
    fun isFavorite(videoId: Long): Flow<Boolean>

    @Query("SELECT videoId FROM favorites")
    suspend fun getAllFavoriteIds(): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteVideoEntity)

    @Query("DELETE FROM favorites WHERE videoId = :videoId")
    suspend fun deleteFavorite(videoId: Long)

    @Query("DELETE FROM favorites")
    suspend fun clearAllFavorites()

    @Query("DELETE FROM favorites WHERE videoId NOT IN (:validIds)")
    suspend fun pruneMissingFavorites(validIds: List<Long>)

    @Query("SELECT * FROM history ORDER BY lastPlayedTimestamp DESC")
    fun getAllHistory(): Flow<List<HistoryVideoEntity>>

    @Query("SELECT playbackPositionMs FROM history WHERE videoId = :videoId LIMIT 1")
    suspend fun getPlaybackPosition(videoId: Long): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHistory(history: HistoryVideoEntity)

    @Query("DELETE FROM history")
    suspend fun clearAllHistory()

    @Query("DELETE FROM history WHERE videoId NOT IN (:validIds)")
    suspend fun pruneMissingHistory(validIds: List<Long>)
}
