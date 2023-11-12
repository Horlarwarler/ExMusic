plugins {
    id("com.android.library")
}

android {
    namespace = "com.crezent.download"
}

apply   {
    from("$rootDir/base-module.gradle")
}

dependencies {
    implementation(project(":core:models"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:design_system"))
}