buildscript {
    val composeVersion by extra("1.6.4")
}

plugins {
    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
}

val cufoonProject by extra("cufoon")

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
