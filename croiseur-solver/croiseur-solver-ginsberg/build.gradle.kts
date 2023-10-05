/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
}

dependencies {
    api(project(":croiseur-common"))
    // For cleaning/filtering the test word list
    testImplementation(project(":croiseur-dictionary:croiseur-dictionary-common"))
}

// Additional mock directory for tests
sourceSets.named("test").configure {
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
