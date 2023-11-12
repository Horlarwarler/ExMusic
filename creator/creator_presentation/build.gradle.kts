plugins {
    id("com.android.library")
}
apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.crezent.creator_presentation"
}

dependencies {
     implementation(project(":core:upload"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:design_system"))
    implementation(project(":core:models"))
    implementation(project(":core:music"))
}