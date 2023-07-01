/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-acceptance-tests-conventions")
    id("com.gitlab.super7ramp.croiseur.java-aggregate-coverage-conventions")
}

dependencies {
    testImplementation(project(":croiseur"))
    testRuntimeOnly(project(":croiseur-dictionary:croiseur-dictionary-hunspell-plugin"))
    testRuntimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    testRuntimeOnly(project(":croiseur-dictionary:croiseur-dictionary-xml-plugin"))
    testRuntimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    testRuntimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
    testRuntimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
    testDictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-hunspell-data"))
    testDictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    testDictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
}

tasks.withType<Test>().configureEach {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}
