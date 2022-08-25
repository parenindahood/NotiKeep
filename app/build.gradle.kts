plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = ProjectConfig.compileSdk
    namespace = ProjectConfig.applicationId

    defaultConfig {
        applicationId = ProjectConfig.applicationId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        resourceConfigurations.addAll(ProjectConfig.locales)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "by.iapsit.notikeep"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.material)
    implementation(libs.coroutines.android)

    implementation(libs.bundles.compose)
    implementation(libs.compose.animation.graphics)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.drawablepainter)

    implementation(libs.navigation.compose)

    implementation(libs.bundles.koin)
    implementation(libs.koin.compose)
    implementation(libs.koin.work)

    implementation(libs.androidx.work)
}
