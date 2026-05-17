# Campus Compass 🧭

**Campus Compass (CC)** is a comprehensive student-centric travel and experience-sharing platform for Android. It empowers college students to document their journeys, share budget-friendly tips, and access critical safety information through a modern, intuitive interface.

## ✨ Key Features

### 🌟 Experience Sharing
- **Rich Post Creation**: Share trip details including venue names, transport modes (Train, Bus, Flight), accommodation, and food costs.
- **Smart Tagging**: Categorize experiences with tags like *#Educational*, *#Adventure*, *#BudgetFriendly*, and more.
- **Visual Storytelling**: High-quality image galleries with captions and cover artwork gradients.

### 🛡️ Safety First: Integrated SOS System
- **Localized Emergency Info**: Each trip post includes specific emergency metadata like nearby hospitals, police stations, and local emergency contacts.
- **One-Tap SOS**: A high-visibility emergency button in the trip details launches a comprehensive SOS Bottom Sheet for immediate assistance.

### 🎨 Modern UI & Branding
- **"CC" Branding**: Custom branded experience from the splash screen to the navigation drawer.
- **Adaptive Icons**: Fully optimized launcher icons for a professional look on all Android versions.
- **Material 3 Design**: A soft, "Light Blue" themed interface with heavily rounded corners (24dp) and fluid animations.
- **Floating Navigation**: A custom-built floating pill navigation bar for seamless screen switching.

### ⚡ Demo Mode
- **Zero-Config Presentation**: The app includes a robust `DEMO_MODE` that bypasses Firebase and uses local mock data.
- **Pre-loaded Demo Accounts**: Instant login with pre-configured personas like "Rider", "Senpai", and "Nomad" to showcase the app without an internet connection.

## 🛠️ Tech Stack

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Language**: [Kotlin](https://kotlinlang.org/)
- **Backend**: [Firebase](https://firebase.google.com/) (Authentication & Firestore)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Compose Navigation with Custom Transitions
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Splash API**: `androidx.core:core-splashscreen`

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or later.
- Android SDK 34 (Upside Down Cake).

### Installation
1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/campus-compass.git
   ```
2. **Open in Android Studio**:
   Allow Gradle to sync and download dependencies.
3. **Run as Demo (Default)**:
   The app is pre-configured in `DEMO_MODE = true` (see `Config.kt`). Simply build and run.
4. **Production Setup**:
   - Set `Config.DEMO_MODE = false`.
   - Add your `google-services.json` to the `app/` folder.
   - Enable Email/Password auth in the Firebase Console.



## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
*Built with ❤️ for the student community by Gyan.*
