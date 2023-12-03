/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java acceptance tests. Basically adds dependency on Cucumber for BDD-style tests.
 */

plugins {
    id("re.belv.croiseur.java")
}

// Hack to make version catalog works with kotlin, see https://github.com/gradle/gradle/issues/15383
val sbom = the<org.gradle.accessors.dm.LibrariesForSbom>()
dependencies {
    testImplementation(sbom.cucumber)
    testImplementation(sbom.cucumber.junit5.engine)
    testImplementation(sbom.junit5.platform.suite)
    testImplementation(sbom.mockito)
    testRuntimeOnly(sbom.cucumber.picocontainer)
}

/**
 * Just as application convention, a dictionary path is necessary.
 */
configurations.register("testDictionaryPath") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.test {
    systemProperty(
        "re.belv.croiseur.dictionary.path",
        configurations.named("testDictionaryPath").get().asPath
    )
}