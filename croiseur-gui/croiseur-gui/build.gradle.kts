/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-application")
    alias(libs.plugins.javafx)
    alias(libs.plugins.conveyor)
}

javafx {
    version = libs.versions.javafx.asProvider().get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":croiseur"))
    implementation(project(":croiseur-gui:croiseur-gui-controller"))
    implementation(project(":croiseur-gui:croiseur-gui-presenter"))
    implementation(project(":croiseur-gui:croiseur-gui-view"))
    implementation(project(":croiseur-gui:croiseur-gui-view-model"))
    runtimeOnly(project(":croiseur-clue:croiseur-clue-openai-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-hunspell-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-xml-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-codec-xd-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-repository-filesystem-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-sat-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-txt-data"))
    dictionaryPath(project(":croiseur-dictionary:croiseur-dictionary-xml-data"))
    // Don't pull hunspell dictionaries, since they are slower to read and are basically the same
    // as XML dictionaries
}

application {
    applicationDefaultJvmArgs = listOf(
        // Memory tuning
        "-Xms256M",                     // initial heap size
        "-Xmx1g",                       // maximum heap size; SAT solver can consume a lot of memory

        // Useful application debug options
        //"-Dre.belv.croiseur.puzzle.path=/your/debug/puzzle/path",

        // Useful Java debug options
        //"-XX:+PrintCommandLineFlags",
        //"-Xlog:gc",                   // print garbage collection events

        // Useful JavaFx debug options
        //"-Dprism.order=sw",           // force sw rendering
        //"-Dprism.verbose=true",       // print rendering pipeline info on startup
        //"-Dprism.showdirty=true",     // show dirty regions
        //"-Djavafx.pulseLogger=true",  // display pulse event information (buggy, JDK-8149490)
        //"-Dsun.awt.disablegrab=true"  // allow breakpoints on JavaFx application thread
    )
}

// Allow ${version} to be expanded in resource bundles
tasks.processResources {
    filesMatching("**/*.properties") {
        expand(project.properties)
    }
}
