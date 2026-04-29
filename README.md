# Skie
Skie is a Weather Forecast Android App that shows real-time weather conditions, hourly forecasts, and a 5-day outlook

---

## Features
- Real-time weather data powered by WeatherAPI
- GPS-based automatic location detection
- Search any city worldwide
- Hourly forecast for the next 12 hours (across midnight)
- 5-day forecast with high/low temperatures and rain chance
- Animated weather icons that match current conditions
- Dynamic gradient sky background per weather condition
- Mood-based weather quotes that change with time of day
- Offline caching

---

## Tech Stack
- **Language:** Kotlin
- **Framework:** Android Jetpack Compose
- **Architecture:** MVVM
- **Tools:** Android Studio
- **Networking:** Retrofit + Gson
- **Local Storage:** Room (SQLite)
- **Preferences:** DataStore
- **Animations:** Lottie Compose
- **Location:** Google Play Services Location
- **API:** WeatherAPI (weatherapi.com)
- **Navigation:** Jetpack Navigation Compose 
- **Async:** Kotlin Coroutines + StateFlow 
- **IDE:** Android Studio 

---
## API
- **WeatherAPI** — [weatherapi.com](https://www.weatherapi.com/)  
  Used for current weather, hourly forecast, 5-day forecast, 
  and city search suggestions.
---
## Animation Highlights
- **Hero Weather Icon** — Lottie animation loops continuously 
  next to the current temperature. Each weather condition 
  (sunny, rainy, stormy, snowy, foggy, cloudy, night) 
  has its own unique Lottie animation
- **5-Day Forecast Cards** — Each card slides in from the right 
  with a staggered delay on every screen load, giving the 
  forecast section a dynamic, layered entrance
- **Search Screen Transition** — The search screen slides up 
  from the bottom using a NavHost enter/exit transition
- **Hourly Forecast Icons** — Static Lottie frames 
  (frozen on last frame) for performance
---
## Dependencies
```kotlin
// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")

// Lottie
implementation("com.airbnb.android:lottie-compose:6.4.0")

// Location
implementation("com.google.android.gms:play-services-location:21.2.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// DataStore
implementation("androidx.datastore:datastore-preferences")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

---


## Test the complete app here:
[Appetize link](https://appetize.io/app/b_hkfubnauwopqdv2lwmsqsmg6de)

---

## View the complete project here:
[GitHub Repo](https://github.com/GraceEunhye19/skie.git)

---
## Screenshots
<img width="200" alt="1" src="https://github.com/user-attachments/assets/00acdeab-4e77-409b-95a4-99ad7bc15803" />
<img width="200" alt="2" src="https://github.com/user-attachments/assets/8f51d5a2-c99b-4e74-815d-64cb367db641" />
<img width="200" alt="3" src="https://github.com/user-attachments/assets/867405eb-d80b-4b28-a975-b8fbd5323dab" />
<img width="200" alt="4" src="https://github.com/user-attachments/assets/710ca067-6952-4893-8f1d-704d59dce343" />
<img width="200" alt="5" src="https://github.com/user-attachments/assets/7ca0e417-922c-4eeb-b93b-791471134635" />
---

## Developer
Grace Igbadun  
Android Developer
