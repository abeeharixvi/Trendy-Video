package com.example

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.data.database.TrendyDatabase
import com.example.data.media.MediaStoreScanner
import com.example.data.preferences.TrendyPreferences
import com.example.data.repository.VideoRepository

class TrendyApplication : Application(), ImageLoaderFactory {

    lateinit var database: TrendyDatabase
        private set

    lateinit var preferences: TrendyPreferences
        private set

    lateinit var repository: VideoRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = TrendyDatabase.getInstance(this)
        preferences = TrendyPreferences(this)
        val scanner = MediaStoreScanner(this)
        repository = VideoRepository(scanner, database.trendyDao())
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("trendy_thumbnails"))
                    .maxSizePercent(0.05)
                    .build()
            }
            .crossfade(true)
            .build()
    }
}
