plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
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

        forEach {
            it.buildConfigField("String", "API_KEY", "\"6aaa8200ce048ed2bee45a85dc8ce851\"")
            it.buildConfigField("String", "API_TOKEN", "\"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\"")
            it.buildConfigField("String", "API_SECRET", "\"4c9701e1037c46aebb8e81bc4b1c26ee\"")
            it.buildConfigField(
                "String",
                "API_BASE",
                "\"https://itp-sv-and5.myshopify.com/admin/api/2023-07/\""
            )
            it.buildConfigField(
                "String",
                "COUNTRIES_API",
                "\"https://countriesnow.space/api/v0.1/\""
            )
            it.buildConfigField(
                "String",
                "EXCHANGE_API",
                "\"https://api.apilayer.com/exchangerates_data/\""
            )
            it.buildConfigField("String", "EXCHANGE_TOKEN", "\"BWdk14fEMOep4ZhNWG5vmizztKVeQJsN\"")
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
        buildConfig = true
        dataBinding = true
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
    implementation(libs.hdodenhof.circleimageview)
    implementation(libs.com.jakewharton.timber.timber)
    implementation(libs.com.google.android.gms.play.services.pay)
    implementation(libs.com.jakewharton.timber.timber)
    implementation(libs.com.google.firebase.firebase.auth.ktx)
    implementation(libs.com.github.bumtech.glide.glide)
    implementation(libs.com.google.android.gms.play.services.auth)
    implementation(libs.com.firebaseui.firebase.ui.auth)
    implementation(libs.com.google.firebase.firebase.firestore.kts)
    implementation(libs.com.google.firebase.firebase.storage.kts)
    implementation(libs.androidx.work.work.runtime.kts)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.play.services)
    implementation(libs.androidx.viewpager2.viewpager2)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)
    implementation(libs.androidx.startup.startup.runtime)
    implementation(libs.android.sdk)
    implementation(libs.scrollingpagerindicator)
    implementation(libs.com.airbnb.android.lottie)
    implementation(libs.scrollingpagerindicator)
    ksp(libs.androidx.room.compiler)
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
    //  implementation(libs.bundles.braintree)


    //testing
    testImplementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)

    testImplementation(libs.org.robolectric.robolectric)
    testImplementation(libs.bundles.hamcrest)
    testImplementation(libs.junit)
    testImplementation(libs.org.mockito.kotlin.mockito.kotlin)
    kaptTest(libs.com.google.dagger.hilt.android.compiler)
    kaptTest(libs.com.google.dagger.hilt.android.testing)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.com.google.dagger.hilt.android.testing)
    kaptAndroidTest(libs.com.google.dagger.hilt.android.compiler)

}