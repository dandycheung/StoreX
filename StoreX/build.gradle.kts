plugins {
    id("com.android.library")
    id("maven-publish")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
}

group = "rommansabbir"
version = "3.1.0"

android {
    namespace = "com.rommansabbir.storex"
    compileSdk = 34

    defaultConfig {
        minSdk = 19 // Use 'minSdk' in Kotlin DSL
        targetSdk = 34 // Use 'targetSdk' in Kotlin DSL

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true // Use 'isIncludeAndroidResources' in Kotlin DSL
        }
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
    implementation("androidx.appcompat:appcompat:1.6.1") // Updated to latest stable version
    implementation("com.google.android.material:material:1.11.0") // Updated to latest stable version
    implementation("androidx.test:core-ktx:1.6.0") // Updated to latest stable version
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0") // Latest stable
    implementation("com.google.code.gson:gson:2.10.1") // Updated to latest stable version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1") // Updated to latest stable version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1") // Updated to latest stable version
    testImplementation("junit:junit:4.13.2") // Already up to date
    testImplementation("org.robolectric:robolectric:4.13") // Updated to latest stable version
}


afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.rommansabbir"
                artifactId = "StoreX"
                version = "3.1.0"
            }
        }
    }
}
