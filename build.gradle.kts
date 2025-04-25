// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) version("8.10.0-alpha07") apply false
    alias(libs.plugins.kotlin.android) version ("2.1.10") apply false
    //id("org.jetbrains.kotlin.plugin.compose") version ("2.1.10") apply false
    id("com.google.devtools.ksp") version "2.1.10-1.0.31" apply false
    alias(libs.plugins.kotlin.compose) version "2.1.10" apply false
}