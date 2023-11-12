plugins {
    id("com.android.library")
}
apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.crezent.user_domain"
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    implementation(project(":core:models"))
    implementation(project(":core:data"))
   implementation(project(":core:database"))
}