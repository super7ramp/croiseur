/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import java.nio.file.Files

plugins {
    id("com.gitlab.super7ramp.croiseur.java-aggregate-coverage-conventions")
    alias(sbom.plugins.javafx)
}

// TODO Find a way to make gradle-extra-module-info work and remove this block (#84).
configurations {
    testRuntimeClasspath {
        attributes { attribute(Attribute.of("javaModule", Boolean::class.javaObjectType), false) }
    }
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
    testImplementation(sbom.testfx.hamcrest)
    testImplementation(sbom.testfx.junit)
    testRuntimeOnly(sbom.testfx.monocle)
    testDictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    testDictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
}

tasks.named<Test>("test") {
    // Export/open JavaFx internals to TestFx: TestFx relies on them.
    /* TODO uncomment these lines when project is modularized again (#84)
    jvmArgs = listOf(
        "--add-exports", "javafx.graphics/com.sun.javafx.application=org.testfx",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=org.testfx",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=org.testfx.monocle"
    )
    */

    // Configure JavaFx/TestFx to run in headless mode, in order to run the tests on CI machines.
    systemProperty("headless.geometry", "1920x1080-32")
    systemProperty("java.awt.headless", true)
    systemProperty("prism.order", "sw")
    systemProperty("testfx.robot", "glass")
    systemProperty("testfx.headless", true)

    // Application properties
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
