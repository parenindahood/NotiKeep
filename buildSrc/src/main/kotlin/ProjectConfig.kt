import org.gradle.api.JavaVersion

/**
 * General project configuration options
 */
object ProjectConfig {

    const val applicationId = "by.iapsit.notikeep"
    const val versionCode = 1
    const val versionName = "1.0"

    val javaVersion = JavaVersion.VERSION_11
    const val jvmTarget = "11"

    const val compileSdk = 33
    const val targetSdk = compileSdk
    const val minSdk = 23

    val locales = listOf("en")
}