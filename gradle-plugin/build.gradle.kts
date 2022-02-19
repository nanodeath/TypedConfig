plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
//    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("TypedConfigPlugin") {
            id = "com.github.nanodeath.typedconfig"
            implementationClass = "com.github.nanodeath.typedconfig.TypedConfigPlugin"
        }
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
