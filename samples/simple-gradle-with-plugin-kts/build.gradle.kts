plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("idea")
    id("application")
    id("com.github.nanodeath.typedconfig") version "0.1"
}

repositories {
    mavenLocal() // This is temporary until TypedConfig is published somewhere
    mavenCentral()
}

application {
    mainClass.set("ApplicationKt")
}

dependencies {
    // None required!
}
