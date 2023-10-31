/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-dictionary"))
    implementation(project(":croiseur-dictionary:croiseur-dictionary-common"))
    implementation(project(":croiseur-dictionary:croiseur-dictionary-hunspell-codec"))
}