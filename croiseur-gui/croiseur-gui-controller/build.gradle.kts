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
    modules = listOf("javafx.graphics")
}

dependencies {
    api(project(":croiseur"))
    implementation(project(":croiseur-gui:croiseur-gui-view-model"))
}