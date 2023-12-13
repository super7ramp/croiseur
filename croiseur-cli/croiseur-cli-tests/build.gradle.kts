/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import java.nio.file.Files

plugins {
    id("re.belv.croiseur.java-aggregate-coverage")
    id("re.belv.croiseur.java-aot")
}

dependencies {
    testImplementation(project(":croiseur-cli:croiseur-cli"))
}

/** Configures tests paths. */
tasks.test {
    systemProperty("re.belv.croiseur.dictionary.path", resolvedDicPath())
    systemProperty("re.belv.croiseur.puzzle.path", testRepoPath())
}

/** Configures native tests paths. */
tasks.nativeTest {
    runtimeArgs.add("-Dre.belv.croiseur.dictionary.path=${resolvedDicPath()}")
    runtimeArgs.add("-Dre.belv.croiseur.puzzle.path=${testRepoPath()}")
}

fun resolvedDicPath(): String {
    return project(":croiseur-cli:croiseur-cli").configurations.getByName("dictionaryPath").asPath
}

fun testRepoPath(): String {
    val tempDir = Files.createTempDirectory("croiseur_test_repo_").toFile()
    tempDir.deleteOnExit()
    return tempDir.path
}