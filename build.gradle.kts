


buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.androidx.navigation.navigation.safe.args.gradle.plugin)
        classpath(libs.com.google.gms.google.services)
    }
}


plugins {
    alias(libs.plugins.android.application) apply  false
    alias(libs.plugins.android.library) apply  false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
