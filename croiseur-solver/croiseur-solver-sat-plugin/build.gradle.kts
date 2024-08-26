/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-solver"))
    implementation(project(":croiseur-solver:croiseur-solver-sat"))
}

spotless {
    java {
        // https://github.com/palantir/palantir-java-format/issues/933
        targetExclude("**/SatSolver.java")
    }
}