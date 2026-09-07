# KiblatSaya

A modern, fast, and ad-free Qibla compass application for Android and Wear OS built with Kotlin Multiplatform, Compose Multiplatform, and Wear Compose.

## Features

- Accurate Direction: Great-circle geodesic calculation based on current GPS coordinates with low-pass sensor filtering for smooth needle movement.
- Haptic Feedback: Tactile vibration when aligned directly towards the Kaaba (within +-3.5 degrees).
- Standalone Wear OS Support: Dedicated Wear OS app with dark OLED dial that works independently without needing continuous phone connection.
- Themes & Palettes: 4 color palettes (Emerald, Lapis, Gold, Ruby) with OLED dark and light mode support.
- Privacy First: No account required, zero tracking, and no ads.
- Offline Mode: Works using device sensors without requiring an active internet connection.

## Project Structure

- `composeApp/`: Phone application (Android & iOS) using Compose Multiplatform.
- `wearApp/`: Smartwatch application for Wear OS using Wear Compose.
- `play_store_assets/`: Store listing graphics, icons, and screenshots.
- `dist/`: Output directory for release binaries (APK and AAB).

## Build and Distribution

Centralized versioning is configured in `gradle/libs.versions.toml`.

### Build Release (APK & AAB)
```bash
./gradlew distributeRelease
```
Outputs in `dist/`:
- `dist/KiblatSaya-v{version}.aab` (Google Play App Bundle - Phone)
- `dist/KiblatSaya-v{version}.apk` (Release APK - Phone)
- `dist/KiblatSaya-WearOS-v{version}.aab` (Google Play App Bundle - Wear OS)
- `dist/KiblatSaya-WearOS-v{version}.apk` (Release APK - Wear OS)

### Build Debug
```bash
./gradlew distributeDebug
```

### Run Tests
```bash
./gradlew test
```

## Privacy Policy

Privacy policy is available at:
https://ashrafsystems.com/privacy#kiblatsaya

## Publisher

Developed by Ashraf Systems Enterprise  
https://ashrafsystems.com
