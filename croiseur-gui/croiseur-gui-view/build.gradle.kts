/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
    alias(libs.plugins.javafx)
}

javafx {
    version = libs.versions.javafx.asProvider().get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    api(project(":croiseur-gui:croiseur-gui-view-model"))
    implementation(platform(libs.ikonli.bom))
    implementation(libs.ikonli.javafx)
    implementation(libs.ikonli.materialdesign2)
}

tasks.processResources {
    // There is no point to include .license files in jar, nobody is going to read them there
    exclude("**/*.license")
}