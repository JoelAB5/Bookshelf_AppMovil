import java.util.Properties

plugins {
    id("com.android.application") version "9.3.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10"
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.isFile) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}
val booksApiKey = localProperties.getProperty("GOOGLE_BOOKS_API_KEY").orEmpty().trim()
    .ifEmpty { providers.environmentVariable("GOOGLE_BOOKS_API_KEY").orNull.orEmpty().trim() }
require(booksApiKey.matches(Regex("[A-Za-z0-9_-]*"))) {
    "GOOGLE_BOOKS_API_KEY contiene caracteres inválidos."
}

android {
    namespace = "com.example.bookshelf"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bookshelf"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "GOOGLE_BOOKS_API_KEY", "\"$booksApiKey\"")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.02.01"))
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}
