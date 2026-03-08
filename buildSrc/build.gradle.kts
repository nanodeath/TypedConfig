plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation("org.jetbrains.kotlinx:binary-compatibility-validator:0.18.1") // keep in sync with libs.versions.toml
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.0.0") // keep in sync with libs.versions.toml
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(21))
    }
}
