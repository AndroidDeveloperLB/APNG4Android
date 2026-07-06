plugins {
    id("com.android.application") version "9.2.1" apply false
    id("com.android.library") version "9.2.1" apply false
    id("org.jetbrains.kotlin.android") version "2.4.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.0" apply false
    id("com.google.devtools.ksp") version "2.3.5" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

val needReplaceLocal = true
if (needReplaceLocal) {
    allprojects {
        configurations.configureEach {
            resolutionStrategy {
                dependencySubstitution {
                    substitute(module("com.github.penfeizhou.android.animation:frameanimation")).using(project(":frameanimation"))
                    substitute(module("com.github.penfeizhou.android.animation:apng")).using(project(":apng"))
                    substitute(module("com.github.penfeizhou.android.animation:awebp")).using(project(":awebp"))
                    substitute(module("com.github.penfeizhou.android.animation:gif")).using(project(":gif"))
                    substitute(module("com.github.penfeizhou.android.animation:glide-plugin")).using(project(":plugin_glide"))
                    substitute(module("com.github.penfeizhou.android.animation:awebpencoder")).using(project(":awebpencoder"))
                    substitute(module("com.github.penfeizhou.android.animation:avif")).using(project(":avif"))
                }
            }
        }
    }
}

extra["Version"] = "3.0.2"

tasks.register("publishFrameAnimation") {
    dependsOn(":frameanimation:publish")
    doLast {
        println("FrameAnimation published")
    }
}

tasks.register("publishAPNG") {
    dependsOn("publishFrameAnimation", ":apng:publish")
    doLast {
        println("APNG published")
    }
}

tasks.register("publishAWebP") {
    dependsOn("publishFrameAnimation", ":awebp:publish")
    doLast {
        println("AWebP published")
    }
}

tasks.register("publishGif") {
    dependsOn("publishFrameAnimation", ":gif:publish")
    doLast {
        println("Gif published")
    }
}

tasks.register("publishAvif") {
    dependsOn("publishFrameAnimation", ":avif:publish")
    doLast {
        println("Avif published")
    }
}

tasks.register("publishAWebPEncoder") {
    dependsOn("publishAWebP", "publishGif", ":awebpencoder:publish")
    doLast {
        println("AWebP Encoder published")
    }
}

tasks.register("publishGlidePlugin") {
    dependsOn("publishAPNG", "publishAWebP", "publishGif", "publishAvif", ":plugin_glide:publish")
    doLast {
        println("GlidePlugin published")
    }
}

tasks.register("PublishAll") {
    dependsOn("publishGlidePlugin", "publishAWebPEncoder")
    doLast {
        println("All published")
    }
}
