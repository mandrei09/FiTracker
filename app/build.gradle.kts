plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.kapt")
}

    android {
        namespace = "com.master.fitnessjourney"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.master.fitnessjourney"
            minSdk = 26
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            buildConfigField("String", "BASE_URL", "\"https://fakestoreapi.com/\"")
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
            buildConfig = true
        }
        kapt {
            correctErrorTypes = true
            useBuildCache = true

        }
    }

    dependencies {
implementation(libs.google.material)
        //        implementation(libs.firebase.firestore.ktx)
        val nav_version = "2.7.7"
        val room_version = "2.6.1"

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)

        implementation("com.android.volley:volley:1.2.1")
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("androidx.room:room-runtime:$room_version")
        //annotationProcessor("androidx.room:room-compiler:$room_version")
        kapt("androidx.room:room-compiler:$room_version")
        implementation("androidx.room:room-ktx:$room_version")

//        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
//        implementation("org.jetbrains.kotlinx:kotlinx-serialization:1.3.0")
//        implementation("com.google.android.material:material:1.11.0")

//        implementation(libs.eventbus)
//                kapt("org.greenrobot:eventbus-annotation-processor:3.0.1")
        implementation("org.greenrobot:eventbus:3.3.1")
        implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    }
