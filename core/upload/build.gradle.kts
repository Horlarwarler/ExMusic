plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply{
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.crezentupload"

}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
     implementation(project(":core:models"))
}