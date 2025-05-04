plugins {
    alias(libs.plugins.android.application) version "8.10.0-alpha07"
    alias(libs.plugins.kotlin.android) version "2.1.10"
    id("com.google.devtools.ksp") version "2.1.10-1.0.31"
    alias(libs.plugins.kotlin.compose) version "2.1.10"
}

android {
    namespace = "com.example.wordwave"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.wordwave"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.translate)
    implementation (libs.androidx.foundation)
    implementation(libs.google.accompanist.systemuicontroller)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.benchmark.macro)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.kotlinx.coroutines.android)

    implementation (libs.ktor.client.core)
    implementation (libs.ktor.client.cio)
    implementation (libs.ktor.client.content.negotiation)
    implementation (libs.ktor.serialization.gson)
    implementation (libs.ktor.client.logging)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // Lifecycle
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)

    // Gson
    implementation (libs.gson)

    implementation (libs.androidx.room.runtime)
    ksp (libs.androidx.room.compiler)
    annotationProcessor (libs.androidx.room.compiler)
    implementation (libs.androidx.room.rxjava2)
    implementation (libs.androidx.room.rxjava3)

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation (libs.androidx.room.guava)

    // optional - Test helpers
    testImplementation (libs.androidx.room.testing)

    // optional - Paging 3 Integration
    implementation (libs.androidx.room.paging)

    implementation (libs.androidx.room.ktx)

}