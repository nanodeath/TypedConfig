package dev.madetobuild.typedconfig.runtime.checks

interface IntegerCheck {
    operator fun invoke(value: Int, name: String)
}
