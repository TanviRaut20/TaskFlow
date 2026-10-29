plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.taskflow"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.taskflow"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:35.1.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    // Android UI
    implementation("androidx.core:core-ktx:<compatible-version>")
    implementation("androidx.appcompat:appcompat:<compatible-version>")
    implementation("com.google.android.material:material:<compatible-version>")
    implementation("androidx.recyclerview:recyclerview:<compatible-version>")

    // Lifecycle and ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:<compatible-version>")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:<compatible-version>")

    // Kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:<compatible-version>")

    // Room / SQLite
    implementation("androidx.room:room-runtime:<compatible-version>")
    implementation("androidx.room:room-ktx:<compatible-version>")
    ksp("androidx.room:room-compiler:<compatible-version>")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:35.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-messaging")

    // Dependency injection
    implementation("com.google.dagger:hilt-android:<compatible-version>")
    ksp("com.google.dagger:hilt-compiler:<compatible-version>")

    // Unit testing
    testImplementation("junit:junit:<compatible-version>")
    testImplementation("io.mockk:mockk:<compatible-version>")
}