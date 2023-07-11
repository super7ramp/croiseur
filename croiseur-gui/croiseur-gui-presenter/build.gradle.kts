/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
    alias(sbom.plugins.javafx)
}

javafx {
    version = sbom.versions.java.get()
    modules = listOf("javafx.graphics")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-presenter"))
    api(project(":croiseur-gui:croiseur-gui-view-model"))
}