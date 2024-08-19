plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.rommansabbir.storexdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rommansabbir.storexdemo"
        minSdk = 21 // No parentheses needed in Kotlin DSL
        targetSdk = 34 // No parentheses needed in Kotlin DSL
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1") // Updated to latest stable version
    implementation("androidx.appcompat:appcompat:1.7.0") // Updated to latest stable version
    implementation("com.google.android.material:material:1.12.0") // Updated to latest stable version
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Already the latest stable version
    implementation(project(path = ":StoreX"))
    testImplementation("junit:junit:4.13.2") // Updated to latest stable version
    androidTestImplementation("androidx.test.ext:junit:1.2.1") // Updated to latest stable version
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1") // Updated to latest stable version
}
