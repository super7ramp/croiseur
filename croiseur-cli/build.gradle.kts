/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import java.nio.file.Files;

plugins {
    id("re.belv.croiseur.java-application")
    id("re.belv.croiseur.java-aot")
    id("re.belv.croiseur.java-aggregate-coverage")
}

dependencies {
    annotationProcessor(sbom.picocli.codegen)
    implementation(sbom.picocli.framework)
    implementation(project(":croiseur"))
    runtimeOnly(project(":croiseur-clue:croiseur-clue-openai-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-hunspell-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-xml-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-codec-xd-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-repository-filesystem-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-sat-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
    runtimeOnly(sbom.slf4j.nop) {
        because("sl4j-api, pulled by a dependency of croiseur-clue-openai-plugin, prints annoying messages otherwise")
    }
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
    // Don't pull hunspell dictionaries, since they are slower to read and are basically the same
    // as XML dictionaries
}

application {
    applicationDefaultJvmArgs = listOf(
        // Memory tuning
        "-Xms64M",                      // initial heap size
        "-Xmx1g",                       // maximum heap size; SAT solver can consume a lot of memory

        // Useful Java debug options
        //"-XX:+PrintCommandLineFlags",
        //"-Xlog:gc",                   // print garbage collection events
    )
}

/**
 * Native build specifics.
 * <p>
 * This is used by picocli-codegen to generates the GraalVM configuration under
 * META-INF/native-image/picocli-generated.
 */
tasks.compileJava {
    val projectId = "${project.group}.${project.name}".replace('-', '.')
    options.compilerArgs.add("-Aproject=${projectId}")
    options.compilerArgs.add("-Averbose")
    options.compilerArgs.add("-Aother.resource.bundles=" +
            "re.belv.croiseur.cli.l10n.Messages," +
            "re.belv.croiseur.clue.openai.plugin.Messages," +
            "re.belv.croiseur.clue.openai.plugin.Prompt," +
            "re.belv.croiseur.solver.ginsberg.plugin.Messages," +
            "re.belv.croiseur.solver.sat.plugin.Messages," +
            "re.belv.croiseur.solver.szunami.plugin.Messages," +
            "re.belv.croiseur.solver.paulgb.plugin.Messages")
    options.compilerArgs.add("-Aother.resource.patterns=.*logging.properties,.*.(dll|dylib|so)")
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
    return configurations.getByName("dictionaryPath").asPath
}

fun testRepoPath(): String {
    val tempDir = Files.createTempDirectory("croiseur_test_repo_").toFile()
    tempDir.deleteOnExit()
    return tempDir.path
}
