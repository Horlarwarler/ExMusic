plugins {
    id("com.android.library")
}
apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.crezent.database"

}

dependencies {
    implementation(project(":core:models"))
}