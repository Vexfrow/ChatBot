import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.dokka") version "1.9.20"
}

android {
    namespace = "fr.c1.chatbot"
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.c1.chatbot"
        minSdk = 26
        targetSdk = 34
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
    testOptions {
        packaging {
            resources {
                excludes += ("META-INF/LICENSE.md")
                excludes += ("META-INF/LICENSE-notice.md")
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.gson)
    implementation(libs.point.of.sale.sdk)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.coil)
    implementation(libs.core.ktx)
    implementation(libs.osmdroid.android)
    implementation(libs.play.services.location)
    implementation(libs.play.services.location.v2101)
    implementation(libs.androidx.preference.ktx)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.androidx.runner)

    implementation(libs.opencsv)
    implementation(libs.kotlin.reflect)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.junit.jupiter)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.intro.showcase.view.introshowcaseview)

    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
    // optional - RxJava2 support
    implementation(libs.androidx.work.rxjava2)
    // optional - GCMNetworkManager support
    implementation(libs.androidx.work.gcm)
    // optional - Test helpers
    androidTestImplementation(libs.androidx.work.testing)
    // optional - Multiprocess support
    implementation(libs.androidx.work.multiprocess)
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        suppressInheritedMembers = true
        outputDirectory = layout.projectDirectory.dir("../Chat-Bot-Doc.github.io")
        documentedVisibilities = Visibility.values().toSet()
        includes.from(fileTree("src/main/java") {
            include("**/package.md")
        })
    }
}