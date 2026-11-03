# ScanFlow — Professional Document Scanner

ScanFlow is an Android document-scanning and document-management app built with Kotlin and Jetpack Compose. The visual direction follows the supplied reference: deep navy surfaces, electric-blue scan actions, colorful tool cards, and a clean light theme.

## Current foundation

- Premium dashboard with quick scan action and document counters.
- Five-section navigation: Home, Scanner, Files, Tools, Profile.
- Dark and light themes.
- Portuguese and English interface labels.
- CameraX live camera preview, photo capture, flash control and front/rear camera switching.
- Gallery image import and Android document picker.
- Private app-local copy of imported and captured documents.
- Local document list, search, favorites, delete, and open/share actions.
- Tools catalogue with clear availability states.

## Technical stack

- Kotlin with Android Gradle Plugin 9.5.0 and Gradle 9.6.0.
- Jetpack Compose + Material 3.
- CameraX 1.6.2.
- AndroidX Lifecycle 2.10.0.
- Local document index stored in app-private storage and SharedPreferences.

The current base is deliberately offline-first. There is no AI service, account system, cloud synchronization, or paid API requirement.

## Build

Open this repository in Android Studio with JDK 17 or newer and Android SDK 36 installed. Let Gradle sync, then run the app configuration on a device or emulator.

On macOS/Linux, build a debug APK with:

    ./gradlew :app:assembleDebug

On Windows:

    gradlew.bat :app:assembleDebug

The APK is generated at app/build/outputs/apk/debug/app-debug.apk.

## Important implementation notes

- Imported files are copied into the app-private Documents directory; the app does not request broad storage access.
- Camera permission is requested only when the Scanner screen is opened.
- The current implementation provides the product shell and real capture/import flows. OCR, PDF page editing/merging/compression, image enhancement, signature placement, vault encryption, and QR/barcode generation are the next feature modules and are not represented as completed functionality yet.
- The document index is currently local metadata storage; Room migration and media thumbnail indexing can be introduced as the library grows.
- Before public release, complete device testing, privacy disclosures, security review, PDF library license review, and Google Play policy checks.

## Project identity

- App name: ScanFlow
- Application ID: com.nexauren.scanflow
- Languages: Portuguese and English
- Minimum Android version: Android 8.0 (API 26)
