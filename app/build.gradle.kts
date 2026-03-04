plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.github.penfeizhou.animation.demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.github.penfeizhou.animation.demo"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.github.bumptech.glide:glide:5.0.5")
    ksp("com.github.bumptech.glide:ksp:5.0.5")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

    val version = rootProject.extra["Version"] as String
    implementation("com.github.penfeizhou.android.animation:awebp:$version")
    implementation("com.github.penfeizhou.android.animation:apng:$version")
    implementation("com.github.penfeizhou.android.animation:gif:$version")
    implementation("com.github.penfeizhou.android.animation:avif:$version")
    implementation("com.github.penfeizhou.android.animation:glide-plugin:$version")
    implementation("com.github.penfeizhou.android.animation:awebpencoder:$version")

    implementation("androidx.compose.ui:ui:1.10.4")
    implementation("androidx.compose.ui:ui-tooling:1.10.4")
    implementation("androidx.compose.foundation:foundation:1.10.4")
    implementation("androidx.compose.material:material:1.10.4")
    implementation("androidx.compose.material:material-icons-core:1.7.8")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.runtime:runtime-livedata:1.10.4")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.10.4")
    implementation("androidx.activity:activity-compose:1.12.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-beta08")
    implementation("com.github.bumptech.glide:avif-integration:5.0.5")
}
