plugins {
    id("com.android.library")
}
apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.crezent.user_presentation"
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":user:user_domain"))
    implementation(project(":core:data"))
    implementation(project(":core:models"))
    implementation(project(":core:download"))
    implementation(project(":core:music"))
    implementation(project(":core:design_system"))
    implementation(project(":core:domain"))

}