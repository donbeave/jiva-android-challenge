buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradleAndroidPlugin}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}")
    }
}

plugins {
    java
    idea
    id("com.adarshr.test-logger") version Versions.gradleTestLoggerPlugin apply false
    kotlin("jvm") version Versions.kotlin apply false
    kotlin("kapt") version Versions.kotlin apply false
}

idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = false
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
