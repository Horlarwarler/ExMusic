plugins {
    id("com.android.library")
}

apply {
    from("$rootDir/compose-module.gradle")
}
android {
    namespace = "com.example.presentation"
}

dependencies {
    implementation(project(":core:design_system"))
    implementation(project(":core:common"))
}