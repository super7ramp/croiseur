/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
    id("re.belv.croiseur.java-benchmark")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-solver"))
    implementation(project(":croiseur-solver:croiseur-solver-sat"))
    jmh(project(":croiseur-solver:croiseur-solver-benchmark"))
}
