/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
    alias(sbom.plugins.javafx)
}

javafx {
    version = sbom.versions.javafx.asProvider().get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    api(project(":croiseur-gui:croiseur-gui-view-model"))
}

tasks.named<ProcessResources>("processResources") {
    // There is no point to include .license files in jar, nobody is going to read them there
    exclude("**/*.license")
}