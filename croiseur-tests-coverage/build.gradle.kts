/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-aggregate-coverage-conventions")
}

dependencies {
    // Aggregates relevant leaf projects: Together, they pull all the code to be covered.
    jacocoAggregation(project(":croiseur-cli"))
    jacocoAggregation(project(":croiseur-gui:croiseur-gui"))
    jacocoAggregation(project(":croiseur-tests"))
}
