plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
}

android {
    namespace = libs.versions.applicationNameSpace.get()
    compileSdk = libs.versions.compileSDK.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.minSDK.get().toInt()
        targetSdk = libs.versions.targetSDK.get().toInt()
        versionCode = libs.versions.codeVersion.get().toInt()
        versionName = libs.versions.compileSDK.get()

        resourceConfigurations.addAll(listOf("en", "ar"))
        testInstrumentationRunner = "com.example.weatherpilot.HiltTestRunner"
    }



    buildTypes {
        val debug by getting {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }

        val release by getting {
            isMinifyEnabled = true
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
        jvmTarget = libs.versions.javaVersion.get()
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat.appcompat)
    implementation(libs.com.google.android.material.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.navigation.fragment)
    implementation(libs.androidx.navigation.navigation.ui.kts)
    implementation(libs.androidx.core.core.splashscreen)
    implementation(libs.com.google.firebase.firebase.auth.ktx)
    implementation(libs.com.google.android.gms.play.services.auth)
    implementation(libs.hdodenhof.circleimageview)
    implementation("com.google.android.gms:play-services-wallet:19.2.0")
    implementation("com.google.android.gms:play-services-pay:16.1.0")
    kapt(libs.androidx.room.compiler)
    kapt(libs.com.google.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.hilt.compiler)




    //bundles
    implementation(libs.bundles.navigation.component)
    implementation(libs.bundles.sdp)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.data.store)
    implementation(libs.bundles.room)
    implementation(libs.bundles.location.maps)


    //testing
    testImplementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)
    testImplementation(libs.org.robolectric.robolectric)
    testImplementation(libs.bundles.hamcrest)
    testImplementation(libs.junit)
    kaptTest(libs.com.google.dagger.hilt.android.compiler)
    kaptTest(libs.com.google.dagger.hilt.android.testing)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.com.google.dagger.hilt.android.testing)
    kaptAndroidTest(libs.com.google.dagger.hilt.android.compiler)

}