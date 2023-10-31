/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-aggregate-coverage")
}

dependencies {
    // Aggregates relevant leaf projects: Together, they pull all the code to be covered.
    jacocoAggregation(project(":croiseur-cli"))
    jacocoAggregation(project(":croiseur-gui:croiseur-gui"))
    jacocoAggregation(project(":croiseur-gui:croiseur-gui-tests"))
    jacocoAggregation(project(":croiseur-tests"))
}
