plugins {
    id("com.android.library")
}
apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.crezent.network"

}

dependencies {

    implementation(project(":core:database"))
    implementation(project(":core:common"))
    implementation(project(":core:models"))
}