/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import java.nio.file.Files

plugins {
    id("com.gitlab.super7ramp.croiseur.java-aggregate-coverage-conventions")
    alias(sbom.plugins.javafx)
}

/**
 * The resolvable dictionary path, where the dictionaries come from.
 */
val testDictionaryPath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

javafx {
    version = sbom.versions.java.get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation(project(":croiseur-gui:croiseur-gui"))
    testImplementation(sbom.testfx.core)
    testImplementation(sbom.testfx.junit)
    testDictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    testDictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
}

tasks.named<Test>("test") {
    jvmArgs = listOf("--add-exports", "javafx.graphics/com.sun.javafx.application=org.testfx")
    systemProperty("com.gitlab.super7ramp.croiseur.dictionary.path", resolvedDicPath())
    systemProperty("com.gitlab.super7ramp.croiseur.puzzle.path", testRepoPath())
}

fun resolvedDicPath(): String {
    return configurations.named("testDictionaryPath").get().asPath
}

fun testRepoPath(): String {
    val tempDir = Files.createTempDirectory("croiseur_test_repo_").toFile()
    tempDir.deleteOnExit()
    return tempDir.path
}
