import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.versions)
    alias(libs.plugins.catalogUpdate)
}

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

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf { UnstableVersionsFilter.rejectVersion(currentVersion, candidate.version) }
}

tasks.register(name = "type", type = Delete::class) {
    delete(rootProject.buildDir)
}
