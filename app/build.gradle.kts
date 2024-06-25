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
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.sdk:point-of-sale-sdk:2.1")
    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")

    implementation(libs.coil)
    implementation(libs.core.ktx)
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation(libs.play.services.location)
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(libs.androidx.preference.ktx)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation("androidx.test:runner:1.5.2")

    implementation("com.opencsv:opencsv:5.5.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val workVersion = "2.9.0"
    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    // optional - RxJava2 support
    implementation("androidx.work:work-rxjava2:$workVersion")
    // optional - GCMNetworkManager support
    implementation("androidx.work:work-gcm:$workVersion")
    // optional - Test helpers
    androidTestImplementation("androidx.work:work-testing:$workVersion")
    // optional - Multiprocess support
    implementation("androidx.work:work-multiprocess:$workVersion")
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        suppressInheritedMembers = true
        documentedVisibilities = Visibility.values().toSet()
        includes.from(fileTree("src/main/java") {
            include("**/package.md")
        })
    }
}