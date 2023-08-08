@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

// このAPI実装ではOpenWeatherMapを利用します
// 1 こちらのサイトからAPI keyを取得します https://home.openweathermap.org/api_keys
// 2 ./apikey.properties にAPI keyを記載します
//     api_key="${your_api_key}" (double-quotation required!)
val apiKey: String = project.file("apikey.properties").let {
    if (it.exists()) {
        val input = FileInputStream(it)
        val props = Properties()
        props.load(input)
        props.getProperty("api_key", "")
    } else ""
}

println("your API key found: $apiKey")

android {
    namespace = "jp.co.yumemi.api"
    compileSdk = 33

    defaultConfig {
        minSdk = 27
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "API_KEY", apiKey)
    }

    buildTypes {
        getByName("release") {
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
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.13.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Test
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
    testImplementation("org.robolectric:robolectric:4.8.2")
    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
}
