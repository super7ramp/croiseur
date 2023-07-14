/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-application-conventions")
    alias(sbom.plugins.javafx)
    alias(sbom.plugins.conveyor)
}

javafx {
    version = sbom.versions.java.get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":croiseur"))
    implementation(project(":croiseur-gui:croiseur-gui-controller"))
    implementation(project(":croiseur-gui:croiseur-gui-presenter"))
    implementation(project(":croiseur-gui:croiseur-gui-view"))
    implementation(project(":croiseur-gui:croiseur-gui-view-model"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-hunspell-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-txt-plugin"))
    runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-xml-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-codec-xd-plugin"))
    runtimeOnly(project(":croiseur-puzzle:croiseur-puzzle-repository-filesystem-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-ginsberg-plugin"))
    runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
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
        "-Xmx512M",                     // maximum heap size

        // Useful application debug options
        //"-Dcom.gitlab.super7ramp.croiseur.puzzle.path=/your/debug/puzzle/path",

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
