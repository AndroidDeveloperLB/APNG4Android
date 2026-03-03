plugins {
    id("com.android.library")
}

android {
    namespace = "com.github.penfeizhou.animation.glide"
    compileSdk = 36

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.github.bumptech.glide:glide:5.0.5")
    annotationProcessor("com.github.bumptech.glide:compiler:5.0.5")
    val version = rootProject.extra["Version"] as String
    api("com.github.penfeizhou.android.animation:awebp:$version")
    api("com.github.penfeizhou.android.animation:apng:$version")
    api("com.github.penfeizhou.android.animation:gif:$version")
    api("com.github.penfeizhou.android.animation:avif:$version")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}

apply(from = rootProject.file("scripts/upload.gradle.kts"))
