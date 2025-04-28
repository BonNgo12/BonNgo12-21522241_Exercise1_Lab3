plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.NewsSentiment"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.NewsSentiment"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
}
dependencies {
    // AndroidX dependencies
    implementation(libs.appcompat) // AndroidX AppCompat library for backward compatibility with older Android versions
    implementation(libs.material) // Material Components for designing UIs following Material Design principles
    implementation(libs.activity) // AndroidX Activity library, used for Activity-related components
    implementation(libs.constraintlayout) // ConstraintLayout for flexible and responsive UI layouts

    // Testing dependencies
    testImplementation(libs.junit) // JUnit for unit testing
    androidTestImplementation(libs.ext.junit) // JUnit extensions for Android testing
    androidTestImplementation(libs.espresso.core) // Espresso for UI testing

    // Retrofit for network operations
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit for handling HTTP requests
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson converter for Retrofit to parse JSON responses

    // Paging library for pagination in RecyclerViews
    implementation("androidx.paging:paging-runtime:3.1.1") // Paging library for implementing pagination in Android apps

    // TensorFlow Lite dependencies for machine learning
    implementation("org.tensorflow:tensorflow-lite:2.11.0") // Core TensorFlow Lite library for running ML models on Android
    implementation("org.tensorflow:tensorflow-lite-support:0.4.3") // Additional support for TensorFlow Lite operations
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.3") // TensorFlow Lite metadata for models

    // Image loading library
    implementation ("com.github.bumptech.glide:glide:4.16.0") // Glide for efficient image loading and caching in Android
}
