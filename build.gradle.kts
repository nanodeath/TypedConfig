plugins {
    kotlin("jvm") version "1.6.10"
}

group = "name.maxaller.konfigurator"
version = "1.0-SNAPSHOT"

allprojects {
    version = rootProject.version
    repositories {
        mavenCentral()
    }
}
