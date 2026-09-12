package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.trendyDataStore: DataStore<Preferences> by preferencesDataStore(name = "trendy_settings")

class TrendyPreferences(private val context: Context) {

    private object PreferencesKeys {
        val AUTOPLAY = booleanPreferencesKey("autoplay")
        val LOOP_VIDEOS = booleanPreferencesKey("loop_videos")
        val START_MUTED = booleanPreferencesKey("start_muted")
        val REMEMBER_POSITION = booleanPreferencesKey("remember_position")
        val SHUFFLE_ENABLED = booleanPreferencesKey("shuffle_enabled")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val autoplay: Flow<Boolean> = context.trendyDataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTOPLAY] ?: true
    }

    val loopVideos: Flow<Boolean> = context.trendyDataStore.data.map { preferences ->
        preferences[PreferencesKeys.LOOP_VIDEOS] ?: true
    }

    val startMuted: Flow<Boolean> = context.trendyDataStore.data.map { preferences ->
        preferences[PreferencesKeys.START_MUTED] ?: true
    }

    val rememberPosition: Flow<Boolean> = context.trendyDataStore.data.map { preferences ->
        preferences[PreferencesKeys.REMEMBER_POSITION] ?: true
    }

    val shuffleEnabled: Flow<Boolean> = context.trendyDataStore.data.map { preferences ->
        preferences[PreferencesKeys.SHUFFLE_ENABLED] ?: false
    }

    val themeMode: Flow<String> = context.trendyDataStore.data.map { preferences ->
        preferences[PreferencesKeys.THEME_MODE] ?: "dark"
    }

    suspend fun setAutoplay(enabled: Boolean) {
        context.trendyDataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTOPLAY] = enabled
        }
    }

    suspend fun setLoopVideos(enabled: Boolean) {
        context.trendyDataStore.edit { preferences ->
            preferences[PreferencesKeys.LOOP_VIDEOS] = enabled
        }
    }

    suspend fun setStartMuted(muted: Boolean) {
        context.trendyDataStore.edit { preferences ->
            preferences[PreferencesKeys.START_MUTED] = muted
        }
    }

    suspend fun setRememberPosition(enabled: Boolean) {
        context.trendyDataStore.edit { preferences ->
            preferences[PreferencesKeys.REMEMBER_POSITION] = enabled
        }
    }

    suspend fun setShuffleEnabled(enabled: Boolean) {
        context.trendyDataStore.edit { preferences ->
            preferences[PreferencesKeys.SHUFFLE_ENABLED] = enabled
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.trendyDataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode
        }
    }
}
