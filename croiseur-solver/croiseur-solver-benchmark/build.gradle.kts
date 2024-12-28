/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-solver"))
    implementation(project(":croiseur-dictionary:croiseur-dictionary-common")) {
        because("It is used to clean/filter the test word list")
    }
    implementation(libs.jmh.annotations)
    implementation(libs.jmh.core)
}

// UKACD is used as word list for benchmarks
tasks.processResources {
    from(
        project(":croiseur-dictionary:croiseur-dictionary-txt-data")
            .layout
            .projectDirectory
            .file("ukacd/UKACD18plus.txt")
    )
}
