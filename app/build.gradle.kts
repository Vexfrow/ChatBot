plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
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
//    region implementation
//    region androidx

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.material3)

//    endregion

//    region google

    implementation(libs.google.material)
    implementation(libs.google.gson)

//    endregion
//    endregion

//    region testImplementation

    testImplementation(libs.junit.jupiter)

//    endregion

//    region androidTestImplementation

//    endregion

    androidTestImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)

    androidTestImplementation(libs.junit.jupiter)

//    debugImplementation

    debugImplementation(libs.androidx.ui.tooling)

//    endregion
}