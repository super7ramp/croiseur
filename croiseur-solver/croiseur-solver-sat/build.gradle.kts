/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    implementation(libs.sat4j.core)
    implementation(libs.sat4j.pb)
    testImplementation(project(":croiseur-dictionary:croiseur-dictionary-common")) {
        because("It is used to clean/filter the test word list")
    }
}

// Additional directory for test utils
sourceSets.named("test") {
    java.srcDir("src/test/util")
}

spotless {
    java {
        // https://github.com/palantir/palantir-java-format/issues/933
        targetExclude("**/Solver.java")
    }
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
