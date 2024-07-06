plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

kapt {
    correctErrorTypes = true
}

android {
    compileSdk = 34
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.athkar.sa"
        minSdk = 29
        targetSdk = 34
        versionCode = 6
        versionName = "3.0.1"

        testInstrumentationRunner = "com.athkar.sa.CustomRunner"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"

        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("appCenter"){
            initWith(getByName("release"))
            applicationIdSuffix = ".appReleaseCenter"
            signingConfig  = signingConfigs.getByName("debug")
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "com.athkar.sa"
}
val lifecycle_version = "2.7.0"
val arch_version = "2.2.0"
val roomVersion = "2.6.1"
val work_version = "2.9.0"
dependencies {

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    testImplementation("junit:junit:4.13.2")

    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.test:core-ktx:1.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycle_version")
    testImplementation("androidx.arch.core:core-testing:$arch_version")

    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    testImplementation("androidx.room:room-testing:$roomVersion")

    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.49")

    androidTestImplementation("com.google.dagger:hilt-android-testing:2.49")
    // ...with Kotlin.
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.49")

    implementation("androidx.datastore:datastore-preferences:1.1.0")
    implementation("androidx.work:work-runtime-ktx:$work_version")
    // optional - Test helpers
    androidTestImplementation("androidx.work:work-testing:$work_version")

    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.9.20")

    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    implementation("androidx.hilt:hilt-work:1.2.0")
    // When using Kotlin.
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    implementation("com.airbnb.android:lottie:5.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    // flex layout google
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.runtime:runtime")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation (platform ("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.6")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.6")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.6")
    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    val media3_version = "1.3.1"
    // exo player 3
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3_version")
    implementation("androidx.media3:media3-ui:$media3_version")
    implementation("androidx.media3:media3-session:$media3_version")

    implementation("androidx.glance:glance-material3:1.0.0")
    implementation("androidx.glance:glance-appwidget:1.0.0")
}