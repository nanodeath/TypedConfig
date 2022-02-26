package dev.madetobuild.typedconfig.runtime.key

/** Rudimentary Key class, mainly for testing. */
data class SimpleKey(override val name: String): Key<String> {
    override fun resolve() = throw UnsupportedOperationException()
}
