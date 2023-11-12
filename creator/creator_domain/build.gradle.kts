plugins {
    id("com.android.library")
}

android {
    namespace = "com.crezent.creator_domain"
}

apply {
    from("$rootDir/base-module.gradle")
}


dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:music"))
    implementation(project(":core:network"))
    implementation(project(":core:design-system"))
    implementation(project(":core:domain"))
}
