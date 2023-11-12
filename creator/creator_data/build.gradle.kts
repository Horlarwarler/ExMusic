plugins {
    id("com.android.library")
}
apply {
    from("$rootDir/base-module.gradle")
}
android {
    namespace = "com.crezent.creator_data"
}


dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
//    implementation(project(":creator:creator_domain"))
}
