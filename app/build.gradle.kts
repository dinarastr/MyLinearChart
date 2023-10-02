plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "ru.dinarastepina.myapplication"
    compileSdk = 33

    defaultConfig {
        applicationId = "ru.dinarastepina.myapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //testing
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    //SciChart dependencies region
    implementation(group = "com.scichart.library", name = "core", version= "3.0.0.4253", ext= "aar")
    implementation(group= "com.scichart.library", name="data", version="3.0.0.4253", ext= "aar")
    implementation(group= "com.scichart.library", name= "drawing", version= "3.0.0.4253", ext= "aar")
    implementation(group= "com.scichart.library", name= "charting", version= "3.0.0.4253", ext= "aar")
    implementation(group= "com.scichart.library", name= "extensions", version = "3.0.0.4253", ext= "aar")
    //end of SciChart dependencies region

    //Dagger2
    implementation("com.google.dagger:dagger:2.46.1")
    kapt("com.google.dagger:dagger-compiler:2.46.1")

    //lifecycle scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
}