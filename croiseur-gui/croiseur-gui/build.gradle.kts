/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-application-conventions")
    alias(sbom.plugins.javafx)
}

javafx {
    version = sbom.versions.java.get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":croiseur"))
    implementation(project(":croiseur-gui:croiseur-gui-controller"))
    implementation(project(":croiseur-gui:croiseur-gui-presenter"))
    implementation(project(":croiseur-gui:croiseur-gui-view"))
    implementation(project(":croiseur-gui:croiseur-gui-view-model"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-hunspell-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-xml-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
    // Don't pull alternative solvers: There is no way to select the solver in GUI for now
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
    // Don't pull hunspell dictionaries, since they are slower to read and are basically the same
    // as XML dictionaries
}
