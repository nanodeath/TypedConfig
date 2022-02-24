package com.github.nanodeath.typedconfig.codegen

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.squareup.kotlinpoet.FileSpec
import java.nio.file.Path

object TypedConfigCli : CliktCommand() {
    private val inputs: List<Path> by argument(help = "One or more *.tc.toml files").path(
        mustExist = true,
        mustBeReadable = true
    )
        .multiple()

    private val outputDirectory: Path by argument(help = "Directory to generate config .kt files").path(
        canBeFile = false,
        canBeDir = true
    )

    private val quiet: Boolean by option(help = "Suppress non-essential text output").flag("-q")

    override fun run() {
        for (path in inputs) {
            val fileSpec: FileSpec = ConfigSpecReader().translateIntoCode(path.toFile())
            fileSpec.writeTo(outputDirectory)
            if (!quiet) {
                echo("Generated ${fileSpec.packageName}.${fileSpec.name} in $outputDirectory", err = true)
            }
        }
    }
}

fun main(args: Array<String>) {
    TypedConfigCli.main(args)
}
