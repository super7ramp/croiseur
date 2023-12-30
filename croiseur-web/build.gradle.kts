/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-application")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)
}

dependencies {
    implementation(project(":croiseur"))
    implementation(libs.spring.web)
    implementation(libs.springdoc.openapi)
    testImplementation(libs.spring.tests)
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-repository-memory-plugin"))
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
}

/** Configures tests paths. */
tasks.test {
    val resolvedDicPath = configurations.named("dictionaryPath").get().asPath
    systemProperty("re.belv.croiseur.dictionary.path", resolvedDicPath)
}
