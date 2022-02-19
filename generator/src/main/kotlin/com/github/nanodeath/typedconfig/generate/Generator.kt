@file:JvmName("Generator")
package com.github.nanodeath.typedconfig.generate

import java.io.File
import java.nio.file.Files

fun main(args: Array<String>) {
    val inputFile = File(requireNotNull(args.getOrNull(0)) { "Input file must be provided" })
    val outputDirectory = File(requireNotNull(args.getOrNull(1)) { "Output directory must be provided" })

    require(inputFile.canRead()) { "Input does not exist or is not readable: $inputFile" }
    Files.createDirectories(outputDirectory.toPath())

    ConfigurationReader()
        .readFile(inputFile)
        .writeTo(outputDirectory)

    System.err.println("Wrote config out to $outputDirectory")
}
