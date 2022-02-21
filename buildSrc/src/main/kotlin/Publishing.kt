import org.gradle.api.publish.maven.MavenPublication

fun MavenPublication.attachApacheLicense() {
    pom.licenses {
        license {
            name.set("Apache-2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            distribution.set("repo")
        }
    }
}
