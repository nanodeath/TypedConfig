package dev.madetobuild.typedconfig.codegen.keys

import com.squareup.kotlinpoet.ClassName

interface KeyGenerator<T : Key<*>> {
    val type: String

    fun mapChecks(checkName: String): ClassName {
        // TODO replace "Unsupported check" with more specific exception
        throw IllegalArgumentException("Unsupported check: $checkName")
    }

    fun generate(key: String, defaultValue: String?, checks: List<ClassName>, metadata: KeyMetadata): T
}
