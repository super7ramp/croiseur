/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-application")
    id("re.belv.croiseur.java-aot")
    id("re.belv.croiseur.java-aggregate-coverage")
}

dependencies {
    annotationProcessor(libs.picocli.codegen)
    implementation(libs.picocli.framework)
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
    runtimeOnly(libs.slf4j.nop) {
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

        // Allow JNI for native solvers
        "--enable-native-access=re.belv.croiseur.solver.paulgb",
        "--enable-native-access=re.belv.croiseur.solver.szunami",

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
    options.compilerArgs.add("-Aother.resource.bundles=re.belv.croiseur.cli.l10n.Messages")
    options.compilerArgs.add("-Aother.resource.patterns=.*logging.properties")
}
