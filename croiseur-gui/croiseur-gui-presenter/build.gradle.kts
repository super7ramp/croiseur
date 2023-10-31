/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
    alias(sbom.plugins.javafx)
}

javafx {
    version = sbom.versions.javafx.asProvider().get()
    modules = listOf("javafx.graphics")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-presenter"))
    api(project(":croiseur-gui:croiseur-gui-view-model"))
}