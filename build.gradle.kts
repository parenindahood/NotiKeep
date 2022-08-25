buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.gradle.plugin.android)
        classpath(libs.gradle.plugin.kotlin.android)
    }
}

tasks.register(name = "type", type = Delete::class) {
    delete(rootProject.buildDir)
}