plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = ProjectConfig.javaVersion
    targetCompatibility = ProjectConfig.javaVersion
}

dependencies {
    implementation(libs.coroutines.android)
}