object Build {
    private const val androidBuildGradleVersion = "8.2.0-beta06"
    const val androidBuildGradle = "com.android.tools.build:gradle:$androidBuildGradleVersion"

    private const val kotlinGradlePluginVersion = "1.8.20"
    const val  kotlinGradlePlugin =  "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinGradlePluginVersion"

   const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.hiltVersion}"
   const val kotlinSerialization =  "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.kotlinVersion}"
}