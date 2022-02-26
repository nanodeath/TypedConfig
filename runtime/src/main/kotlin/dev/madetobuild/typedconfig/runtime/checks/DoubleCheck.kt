package dev.madetobuild.typedconfig.runtime.checks

interface DoubleCheck {
    operator fun invoke(value: Double, name: String)
}
