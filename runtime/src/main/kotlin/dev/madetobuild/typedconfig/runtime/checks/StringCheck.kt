package dev.madetobuild.typedconfig.runtime.checks

interface StringCheck {
    operator fun invoke(value: String, name: String)
}
