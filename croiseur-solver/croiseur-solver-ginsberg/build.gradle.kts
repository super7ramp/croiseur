/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    api(project(":croiseur-common"))
    testImplementation(project(":croiseur-dictionary:croiseur-dictionary-common")) {
        because("It is used to clean/filter the test word list")
    }
}

// Additional mock directory for tests
sourceSets.named("test") {
    java.srcDir("src/test/mock")
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
