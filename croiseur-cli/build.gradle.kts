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

/**
 * Native build specifics.
 * <p>
 * Things that cannot be configured via picocli-codegen, e.g. JNI.
 */
graalvmNative {
    binaries {
        named("main") {
            val projectId = "${project.group}.${project.name}".replace('-', '.')
            val jniConfPath = "${project.buildDir}/resources/main/META-INF/native-image/manual/${projectId}/jni-config.json"
            buildArgs.add("-H:JNIConfigurationFiles=${jniConfPath}")
        }
    }
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

tasks.named<Test>("test") {
    systemProperty(
        "com.gitlab.super7ramp.croiseur.dictionary.path",
        configurations.named("dictionaryPath").get().asPath
    )
}
