/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
}

dependencies {
    implementation(sbom.sat4j.core)
    // For the test word list
    testImplementation(project(":croiseur-dictionary:croiseur-dictionary-common"))
}

tasks.test {
    // Solver uses a lot of memory
    maxHeapSize = "2g"
}
