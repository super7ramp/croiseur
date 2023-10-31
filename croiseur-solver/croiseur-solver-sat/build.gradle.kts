/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    implementation(sbom.sat4j.core)
    implementation(sbom.sat4j.pb)
    // For cleaning/filtering the test word list
    testImplementation(project(":croiseur-dictionary:croiseur-dictionary-common"))
    testImplementation(sbom.junit5.params)
}

// UKACD is used as test word list
tasks.processTestResources {
    from(
        project(":croiseur-dictionary:croiseur-dictionary-txt-data")
            .layout
            .projectDirectory
            .file("ukacd/UKACD18plus.txt")
    )
}

tasks.test {
    // Solver uses a lot of memory
    maxHeapSize = "2g"
}
