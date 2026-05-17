# Campus Compass 🧭

**Campus Compass** is a modern Android application designed for college students to share and discover travel experiences. Built with **Jetpack Compose**, **Material 3**, and **Firebase**, it provides a soft, intuitive UI for navigating both educational and leisure trips.

## 🚀 Features

- **Main Feed Hub:** A centralized display of student-shared trips with a hybrid grid/list layout.
- **Smart SOS System:** A one-tap emergency system providing quick access to campus security and local authorities via a Modal Bottom Sheet.
- **Experience Publishing:** Students can share their own travel stories, transport modes, and tips.
- **Filtering System:** Easily categorize trips into *Educational*, *Leisure*, or *Events*.
- **Floating Navigation:** A custom floating pill-shaped navigation bar for a premium, modern feel.
- **Offline Caching (In Progress):** Designed to work in low-connectivity environments.

## 🛠️ Architecture (Based on DFD)

The app follows a structured Data Flow Diagram (DFD):
- **Process 1.0:** Guest/Student Authentication (Firebase Auth).
- **Process 2.0:** Main Feed Screen coordination.
- **Process 3.0:** Post Engine (Firestore integration).
- **Process 4.0:** Create Post Flow.
- **Process 5.0:** Smart SOS System.
- **Process 6.0:** Navigation Drawer & Routing.

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Backend:** Firebase (Auth, Firestore, Analytics)
- **Architecture:** MVVM (ViewModel, LiveData/State)
- **Navigation:** Compose Navigation

## 📸 UI/UX Design

The app uses a "Soft Blue & White" visual language:
- **Primary Color:** `#1A73E8` (PrimaryBlue)
- **Surface:** `#E8F0FE` (LightBlueSurface)
- **Rounding:** 24.dp heavily rounded corners for a modern, approachable feel.

## 🏗️ Getting Started

1. **Clone the repo:**
   ```bash
   git clone https://github.com/your-username/campus-compass.git
   ```
2. **Setup Firebase:**
   - Create a project in the [Firebase Console](https://console.firebase.google.com/).
   - Add an Android app with the package name `com.gyan.campuscompass`.
   - Download `google-services.json` and place it in the `app/` directory.
   - Enable **Anonymous Authentication** and **Firestore** (in test mode).
3. **Build & Run:** Open the project in Android Studio (Ladybug or later) and run on an API 34+ emulator.

## 📄 License

This project is licensed under the MIT License.
