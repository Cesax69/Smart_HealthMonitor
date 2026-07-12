plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "mx.utng.smarthealthmonitor.tv"
    compileSdk = 35
    defaultConfig {
        applicationId = "mx.utng.smarthealthmonitor.tv"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose for TV
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Leanback Library (Keeping for transition if needed, though session focuses on Compose)
    implementation("androidx.leanback:leanback:1.2.0")
    
    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    
    // Shared Module
    implementation(project(":shared"))
    
    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    
    // Fragment KTX
    implementation("androidx.fragment:fragment-ktx:1.8.0")
    
    // Transitive dependencies
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    
    // Room Compiler
    ksp(libs.androidx.room.compiler)
}
