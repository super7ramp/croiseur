/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-application-conventions")
    alias(sbom.plugins.spring.boot)
    alias(sbom.plugins.spring.deps)
}

dependencies {
    implementation(project(":croiseur"))
    implementation(sbom.spring.web)
    testImplementation(sbom.spring.tests)
    runtimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-repository-memory-plugin"))
}