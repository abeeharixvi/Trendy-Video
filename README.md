# TRENDY

> **Your Videos. Your Phone. Your Feed.**

Trendy is a private, 100% offline, native Android vertical video player designed in the engaging style of modern TikTok and Instagram Reels — strictly for videos already stored on your Android device.

---

## 🌟 Key Highlights

- **📱 True Native Android**: Built from the ground up using Kotlin, Jetpack Compose, Material 3, and Android MediaStore.
- **🔒 Privacy First & 100% Offline**: No network connection required. No video uploads, zero server tracking, zero accounts, and zero telemetry. All videos and metadata stay securely on your physical device.
- **⚡ High-Performance Vertical Feed**: Smooth snap vertical scrolling powered by Compose `VerticalPager` and Jetpack Media3 / ExoPlayer.
- **🎯 Lightweight Memory Footprint**: Strictly reuses player instances rather than instantiating hundreds of video decoders.
- **❤️ Local Favorites & Watch History**: Offline local database using Android Room to keep track of liked videos and remember last playback positions.
- **🎛️ Rich Controls**:
  - Tap anywhere to toggle Play/Pause
  - Double-tap or heart icon to Like with animated feedback
  - Mute / Unmute global toggle with persistent preferences
  - Fullscreen / Zoom aspect toggle
  - Video info bottom sheet (resolution, duration, file size, MIME type, local URI)
  - Auto-fading controls with interactive progress scrubber
  - Shuffle mode and loop mode

---

## 🏗️ Architecture & Project Structure

The project strictly follows Android Architecture Guidelines and the Repository Pattern:

```
trendy/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt               # Entry point, lifecycle observer & permission flow
│   │   │   │   ├── TrendyApplication.kt          # Application class, Coil video decoder config
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── VideoItem.kt          # Domain model for local device videos
│   │   │   │   │   │   └── VideoSortOrder.kt     # Sorting enum (Newest, Oldest, Name, Duration)
│   │   │   │   │   ├── database/
│   │   │   │   │   │   ├── FavoriteVideoEntity.kt# Room entity for liked videos
│   │   │   │   │   │   ├── HistoryVideoEntity.kt # Room entity for playback position & history
│   │   │   │   │   │   ├── TrendyDao.kt          # Room Data Access Object with reactive Flows
│   │   │   │   │   │   └── TrendyDatabase.kt     # Local SQLite database
│   │   │   │   │   ├── media/
│   │   │   │   │   │   └── MediaStoreScanner.kt  # Background query engine for device MediaStore
│   │   │   │   │   ├── preferences/
│   │   │   │   │   │   └── TrendyPreferences.kt  # DataStore for user settings (autoplay, loop, muted)
│   │   │   │   │   └── repository/
│   │   │   │   │       └── VideoRepository.kt    # Data orchestration, pruning missing videos
│   │   │   │   ├── player/
│   │   │   │   │   ├── TrendyPlaybackState.kt    # Immutable state for player progress & errors
│   │   │   │   │   ├── TrendyPlayerManager.kt    # Jetpack Media3 ExoPlayer controller & pool
│   │   │   │   │   └── VideoPlayerView.kt        # Compose AndroidView wrapper for PlayerView
│   │   │   │   └── ui/
│   │   │   │       ├── theme/                    # Trendy neon-obsidian dark palette & theme
│   │   │   │       ├── navigation/               # Type-safe bottom navigation and routes
│   │   │   │       ├── components/               # VideoThumbnail, TrendyBottomBar, MetadataSheet
│   │   │   │       ├── feed/                     # Fullscreen TikTok-style Feed & interactive overlay
│   │   │   │       ├── library/                  # Video grid with search, sort, and rescan
│   │   │   │       ├── favorites/                # Liked videos gallery
│   │   │   │       ├── settings/                 # Playback preferences, theme, data management
│   │   │   │       ├── permission/               # Privacy-focused permission explanation screen
│   │   │   │       └── viewmodel/                # MainViewModel & ViewModelFactory
│   │   │   ├── res/                              # Adaptive icons, drawables, strings
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   └── build.gradle.kts
├── gradle/libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🛠️ Build & Compilation

### Requirements
- Android Studio Ladybug / Meerkat or newer
- JDK 17 or JDK 21
- Android SDK 36 (compileSdk) with minSdk 24 (Android 7.0+)

### Building the APK

To build the **Debug APK**:
```bash
./gradlew assembleDebug
```
The output APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

To build the **Release APK**:
```bash
./gradlew assembleRelease
```
The output APK will be located at:
`app/build/outputs/apk/release/app-release.apk`

### Configuring Release Signing Keystore
To sign a release APK for distribution:
1. Generate a keystore with `keytool`:
   ```bash
   keytool -genkey -v -keystore my-upload-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
   ```
2. Set environment variables prior to running the release build:
   ```bash
   export KEYSTORE_PATH="/path/to/my-upload-key.jks"
   export STORE_PASSWORD="your-store-password"
   export KEY_PASSWORD="your-key-password"
   ./gradlew assembleRelease
   ```
*(Never commit `.jks` keystores or password credentials to source control).*

---

## 🔐 Permissions Model

Trendy uses the modern Android granular permissions model:
- **Android 13+ (API 33+)**: Requests only `android.permission.READ_MEDIA_VIDEO`.
- **Android 12 and below (API 24–32)**: Requests `android.permission.READ_EXTERNAL_STORAGE` capped with `android:maxSdkVersion="32"`.
- Trendy never requests `WRITE_EXTERNAL_STORAGE` or unnecessary location/camera permissions.
- Videos are read solely from their content URIs (`content://media/external/video/media/...`).

---

## 📄 License & Privacy Notice
All video files remain exclusively on your device. Trendy does not collect personal identifiers, analytics, device identifiers, or media data.

---

## 👨‍💻 Developer Information

**Developed by Arbab Rizvi**

- 📞 **030383631699**
- ✉️ **arbabrixvi@gmail.com**

*(Attribution information only. Trendy does not collect, track, or transmit any user or contact information).*

