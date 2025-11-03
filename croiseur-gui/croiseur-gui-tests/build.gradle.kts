/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import java.nio.file.Files

plugins {
    id("re.belv.croiseur.java-aggregate-coverage")
    alias(libs.plugins.javafx)
}

javafx {
    version = libs.versions.javafx.asProvider().get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

val testDictionaryPath by configurations.registering

dependencies {
    testImplementation(project(":croiseur-gui:croiseur-gui"))
    testImplementation(libs.testfx.core)
    testImplementation(libs.testfx.hamcrest)
    testImplementation(libs.testfx.junit)
    testRuntimeOnly(libs.testfx.monocle)
    testDictionaryPath(project(":croiseur-gui:croiseur-gui", "dictionaryPath"))
}

tasks.test {
    jvmArgs = listOf(
        // Allow JNI for native solvers
        "--enable-native-access=ALL-UNNAMED",
        /* TODO uncomment these lines when project is modularized again (#84)
        "--enable-native-access=re.belv.croiseur.solver.paulgb",
        "--enable-native-access=re.belv.croiseur.solver.szunami",
         */

        // Export/open JavaFx internals to TestFx: TestFx relies on them.
        /* TODO uncomment these lines when project is modularized again (#84)
        "--add-exports", "javafx.graphics/com.sun.javafx.application=org.testfx",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=org.testfx",
        "--add-opens", "javafx.graphics/com.sun.glass.ui=org.testfx.monocle"
         */
    )

    // Configure JavaFx/TestFx to run in headless mode, in order to run the tests on CI machines.
    systemProperty("headless.geometry", "1920x1080-32")
    systemProperty("java.awt.headless", true)
    systemProperty("prism.order", "sw")
    systemProperty("testfx.robot", "glass")
    systemProperty("testfx.headless", true)

    // Application properties
    systemProperty("re.belv.croiseur.dictionary.path", resolvedDicPath())
    systemProperty("re.belv.croiseur.puzzle.path", testRepoPath())
}

fun resolvedDicPath(): String {
    return testDictionaryPath.get().asPath
}

fun testRepoPath(): String {
    val tempDir = Files.createTempDirectory("croiseur_test_repo_").toFile()
    tempDir.deleteOnExit()
    return tempDir.path
}
