plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("idea")
    id("application")
    id("dev.madetobuild.typedconfig") version "0.3-SNAPSHOT"
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
