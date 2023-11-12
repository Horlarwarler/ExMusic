plugins {
    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.crezent.design_system"
}
apply{
    from("$rootDir/compose-module.gradle")
}
dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:models"))
}