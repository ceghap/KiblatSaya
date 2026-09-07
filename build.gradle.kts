plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
}

tasks.register("distributeRelease") {
    group = "distribution"
    description = "Builds Phone & Wear OS release binaries and distributes to root dist/."
    dependsOn(":composeApp:distributeRelease", ":wearApp:distributeRelease")
}

tasks.register("distributeDebug") {
    group = "distribution"
    description = "Builds Phone & Wear OS debug binaries and distributes to root dist/."
    dependsOn(":composeApp:distributeDebug", ":wearApp:distributeDebug")
}
