plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
apply{
    from("$rootDir/base-module.gradle")

}
android {
    namespace = "com.crezent.music"

}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:models"))
}