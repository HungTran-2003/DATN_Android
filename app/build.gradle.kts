plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "haui.do_an.moive_ticket_booking"
    compileSdk = 35

    defaultConfig {
        applicationId = "haui.do_an.moive_ticket_booking"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", apiKey().toString())
        buildConfigField("String", "CLIEND_ID", clientId().toString())
        buildConfigField("String", "API_KEY_PAYQR", apikeyPayQr().toString())
        buildConfigField("String", "CHECKSUM", checksum().toString())


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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

fun apiKey() = rootProject.file("local.properties").readLines().find { it.startsWith("API_KEY") }?.split("=")?.get(1)
fun clientId() = rootProject.file("local.properties").readLines().find { it.startsWith("CLIEND_ID") }?.split("=")?.get(1)
fun apikeyPayQr() = rootProject.file("local.properties").readLines().find { it.startsWith("API_KEY_PAYQR") }?.split("=")?.get(1)
fun checksum() = rootProject.file("local.properties").readLines().find { it.startsWith("CHECKSUM") }?.split("=")?.get(1)
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}

