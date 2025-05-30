// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) version("8.10.0") apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.kotlin.compose) apply false
}