pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "APNG4Android"

include(":app", ":apng", ":awebp", ":frameanimation", ":plugin_glide", ":gif", ":awebpencoder", ":avif")
