import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    //alias(libs.plugins.kotlin.android) unnecessary because kotlin-compose in libs handles both compose and android
    id("com.google.devtools.ksp")
    id("androidx.room")
}

//to manually load the local.properties file here and call it in buildconfig
// 1. Initialize the Properties object
val localProperties = Properties()

// 2. Locate and load the file from the project root
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.example.skie"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }
    sourceSets {
        getByName("main") {
            assets.srcDirs("src/main/assets")
        }
    }

    defaultConfig {
        applicationId = "com.example.skie"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            //"\"${project.findProperty("WEATHER_API_KEY") ?: ""}\"" to access gradle.properties
            "\"${localProperties.getProperty("WEATHER_API_KEY") ?:""}\"" //to access local.properties
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //room(old)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //viewmodel & navigation
    implementation(libs.androidx.navigation.compose)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")

// Lottie
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    // Location
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // Coroutines & preferences
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation(libs.androidx.datastore.preferences)

    //for the gps
    implementation("com.google.android.gms:play-services-location:21.2.0")

    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.compose.ui:ui-text-google-fonts")

}
room {
    schemaDirectory("$projectDir/schemas")
}