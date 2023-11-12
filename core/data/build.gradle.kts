plugins {
    id("com.android.library")
}

android {
    namespace = "com.crezent.data"
}
apply {
    from("$rootDir/base-module.gradle")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:models"))
}