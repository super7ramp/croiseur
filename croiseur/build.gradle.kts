/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-clue"))
    api(project(":croiseur-spi:croiseur-spi-dictionary"))
    api(project(":croiseur-spi:croiseur-spi-puzzle-codec"))
    api(project(":croiseur-spi:croiseur-spi-puzzle-repository"))
    api(project(":croiseur-spi:croiseur-spi-solver"))
    api(project(":croiseur-spi:croiseur-spi-presenter"))
}