/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-dictionary"))
    api(project(":croiseur-spi:croiseur-spi-solver"))
    api(project(":croiseur-spi:croiseur-spi-presenter"))
}