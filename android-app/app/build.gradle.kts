plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion(Apps.buildTools)

    defaultConfig {
        applicationId = "com.zhokhov.jiva.challenge"
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName
        multiDexEnabled = false
        testInstrumentationRunner = "com.zhokhov.jiva.challenge.MyCustomTestRunner"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk7"))

    // Android
    implementation("android.arch.lifecycle:reactivestreams:1.1.1")
    testImplementation("android.arch.core:core-testing:1.1.1")

    // AndroidX
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.annotation:annotation:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.security:security-crypto:1.1.0-alpha02")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:core-ktx:1.3.0")

    // AndroidX Hilt
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:${Versions.androidXHilt}")
    kapt("androidx.hilt:hilt-compiler:${Versions.androidXHilt}")

    // Material Design
    implementation("com.google.android.material:material:${Versions.material}")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
    androidTestImplementation("com.google.dagger:hilt-android-testing:${Versions.hilt}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${Versions.hilt}")
    testImplementation("com.google.dagger:hilt-android-testing:${Versions.hilt}")
    kaptTest("com.google.dagger:hilt-android-compiler:${Versions.hilt}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-jackson:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}")

    // Jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:${Versions.okHttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}")

    // RxJava
    implementation("io.reactivex.rxjava3:rxandroid:${Versions.rxAndroid}")
    implementation("io.reactivex.rxjava3:rxjava:${Versions.rxJava}")

    // Timber
    implementation("com.jakewharton.timber:timber:${Versions.timber}")

    // JUnit
    testImplementation("junit:junit:${Versions.junit}")

    // Truth
    testImplementation("com.google.truth:truth:${Versions.truth}")
    androidTestImplementation("com.google.truth:truth:${Versions.truth}")

    // Mockito
    testImplementation("org.mockito:mockito-core:${Versions.mockito}")

    // LeakCanary
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}")
}

kapt {
    correctErrorTypes = true
}
