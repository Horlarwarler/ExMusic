plugins {
    id("com.android.library")
}
apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.crezent.common"

}

dependencies {

    implementation(project(":core:database"))
}