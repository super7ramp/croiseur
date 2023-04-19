/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-conventions")
}

dependencies {
    implementation(project(":croiseur-dictionary:croiseur-dictionary-common"))
    implementation(project(":croiseur-dictionary:croiseur-dictionary-hunspell-codec"))
    implementation(project(":croiseur-dictionary:croiseur-dictionary-xml-codec"))
}