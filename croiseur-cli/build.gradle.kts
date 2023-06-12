/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-cli-conventions")
}

dependencies {
    implementation(project(":croiseur"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-hunspell-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-xml-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
    // Don't pull hunspell dictionaries, since they are slower to read and are basically the same
    // as XML dictionaries
}

application {
    applicationDefaultJvmArgs = listOf(
        // Memory tuning
        "-Xms64M",                      // initial heap size
        "-Xmx384M",                     // maximum heap size

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
tasks.named<JavaCompile>("compileJava") {
    options.compilerArgs.add("-Aother.resource.bundles=" +
            "com.gitlab.super7ramp.croiseur.cli.l10n.Messages," +
            "com.gitlab.super7ramp.croiseur.solver.ginsberg.plugin.Messages," +
            "com.gitlab.super7ramp.croiseur.solver.szunami.plugin.Messages," +
            "com.gitlab.super7ramp.croiseur.solver.paulgb.plugin.Messages")
    options.compilerArgs.add("-Aother.resource.patterns=.*logging.properties,.*.(dll|dylib|so)")
}

/** Configures tests dictionary path: Application needs it to find dictionaries. */
tasks.named<Test>("test") {
    systemProperty("com.gitlab.super7ramp.croiseur.dictionary.path", resolvedDicPath())
}

/** Configures native tests dictionary path: Native application needs it to find dictionaries. */
tasks.named<org.graalvm.buildtools.gradle.tasks.NativeRunTask>("nativeTest") {
    runtimeArgs.add("-Dcom.gitlab.super7ramp.croiseur.dictionary.path=${resolvedDicPath()}")
}

fun resolvedDicPath(): String {
    return configurations.named("dictionaryPath").get().asPath
}
