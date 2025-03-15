plugins {
    alias(libs.plugins.android.application) version "8.8.0-alpha05"
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

    val roomVersion = "2.6.1"

    implementation ("androidx.room:room-runtime:${roomVersion}")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See KSP Quickstart to add KSP to your build
    ksp ("androidx.room:room-compiler:${roomVersion}")

    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor ("androidx.room:room-compiler:${roomVersion}")

    // optional - RxJava2 support for Room
    implementation ("androidx.room:room-rxjava2:${roomVersion}")

    // optional - RxJava3 support for Room
    implementation ("androidx.room:room-rxjava3:${roomVersion}")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation ("androidx.room:room-guava:${roomVersion}")

    // optional - Test helpers
    testImplementation ("androidx.room:room-testing:${roomVersion}")

    // optional - Paging 3 Integration
    implementation ("androidx.room:room-paging:${roomVersion}")

    implementation ("androidx.room:room-ktx:${roomVersion}")

}