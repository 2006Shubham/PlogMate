plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // Firebase plugin
}

android {
    namespace = "com.example.megamart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.megamart"
        minSdk = 31
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.drawerlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth)

    // Additional dependencies
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation("com.loopj.android:android-async-http:1.4.11")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.zxing:core:3.5.1")
    implementation("com.google.firebase:firebase-firestore:25.1.3")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")

    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs. espresso.core)

    implementation("com.google.zxing:core:3.5.1")
    implementation("com.google.firebase:firebase-firestore:25.1.3")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.loopj.android:android-async-http:1.4.11")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.zxing:core:3.5.1")
    implementation("com.google.firebase:firebase-firestore:25.1.3")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")



    // Google Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.0.0")

    // Google Play Services Location (if you are using location-related services)
    implementation("com.google.android.gms:play-services-location:18.0.0")

    // Google Places API for location selection (optional)
    implementation("com.google.android.libraries.places:places:3.0.0")

    implementation("com.google.android.libraries.places:places:2.7.0")



}
