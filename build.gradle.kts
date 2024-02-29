buildscript {
    val composeVersion by extra("1.5.4")
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}

val CufoonProject by extra("cufoon")

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
