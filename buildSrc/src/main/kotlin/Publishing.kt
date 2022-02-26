import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByType

fun MavenPublication.attachCommonPomMetadata() {
    pom.url.set("https://github.com/nanodeath/TypedConfig")
    pom.developers {
        developer {
            id.set("nanodeath")
            name.set("Max Aller")
            email.set("max@madetobuild.dev")
        }
    }
    pom.scm {
        connection.set("scm:git:git://github.com/nanodeath/TypedConfig.git")
        developerConnection.set("scm:git:ssh://git@github.com/nanodeath/TypedConfig.git")
        url.set(pom.url)
    }
    pom.licenses {
        license {
            name.set("Apache-2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            distribution.set("repo")
        }
    }
}

fun Project.addSonatypeRepository() {
    val publishing = extensions.getByType<PublishingExtension>()
    publishing.repositories.maven {
        val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        credentials {
            username = System.getenv("SONATYPE_USERNAME")
            password = System.getenv("SONATYPE_PASSWORD")
        }
    }
}

val IsCI get() = "true".equals(System.getenv("CI"), ignoreCase = true)
