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

tasks.register<JavaExec>("scorer") {
    group = "Dictionary"
    description = "Give a score to a given dictionary corresponding to the capability of its words to cross with each other"
    mainClass.set("com.gitlab.super7ramp.croiseur.dictionary.tools.Scorer")
    mainModule.set("com.gitlab.super7ramp.croiseur.dictionary.tools")
    classpath = sourceSets.getByName("main").runtimeClasspath
}
