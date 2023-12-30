/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-aggregate-coverage")
}

configurations.register("testDictionaryPath") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    testImplementation(project(":croiseur"))
    testImplementation(libs.cucumber)
    testImplementation(libs.cucumber.junit5.engine)
    testImplementation(libs.junit5.platform.suite)
    testImplementation(libs.mockito)
    testRuntimeOnly(project(":croiseur-dictionary:croiseur-dictionary-hunspell-plugin"))
    testRuntimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    testRuntimeOnly(project(":croiseur-dictionary:croiseur-dictionary-xml-plugin"))
    testRuntimeOnly(project(":croiseur-puzzle:croiseur-puzzle-codec-xd-plugin"))
    testRuntimeOnly(project(":croiseur-puzzle:croiseur-puzzle-repository-memory-plugin"))
    testRuntimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    testRuntimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
    testRuntimeOnly(project(":croiseur-solver:croiseur-solver-sat-plugin"))
    testRuntimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
    testRuntimeOnly(libs.cucumber.picocontainer)
    "testDictionaryPath"(project(":croiseur-dictionary:croiseur-dictionary-hunspell-data"))
    "testDictionaryPath"(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    "testDictionaryPath"(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
}

tasks.test {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
    systemProperty(
        "re.belv.croiseur.dictionary.path",
        configurations.getByName("testDictionaryPath").asPath
    )
}
