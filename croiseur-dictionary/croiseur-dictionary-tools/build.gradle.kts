/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java")
}

dependencies {
    implementation(project(":croiseur-dictionary:croiseur-dictionary-common"))
    implementation(project(":croiseur-dictionary:croiseur-dictionary-hunspell-codec"))
    implementation(project(":croiseur-dictionary:croiseur-dictionary-xml-codec"))
}

tasks.register<JavaExec>("basicScorer") {
    group = "Dictionary"
    description = "Give a score to a given dictionary corresponding to the capability of its words to cross with each other"
    mainClass.set("re.belv.croiseur.dictionary.tools.BasicScorer")
    mainModule.set("re.belv.croiseur.dictionary.tools")
    classpath = sourceSets.getByName("main").runtimeClasspath
}

tasks.register<JavaExec>("squareSolutionEstimator") {
    group = "Dictionary"
    description = "Give an estimated number of solutions for various square grids for the given dictionary"
    mainClass.set("re.belv.croiseur.dictionary.tools.SquareSolutionEstimator")
    mainModule.set("re.belv.croiseur.dictionary.tools")
    classpath = sourceSets.getByName("main").runtimeClasspath
}
