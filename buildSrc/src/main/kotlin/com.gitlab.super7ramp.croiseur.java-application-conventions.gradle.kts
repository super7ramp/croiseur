/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import java.nio.file.Files

/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java applications.
 */

plugins {
    id("application")
    id("com.gitlab.super7ramp.croiseur.java-conventions")
}

/**
 * The resolvable dictionary path, where the dictionaries come from.
 */
configurations.register("dictionaryPath") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

/**
 * The installation dictionary directory, relative to destination directory, basically
 * $(datadir)/dictionaries
 */
val dictionaryDir = project.property("datadir") as String + File.separator + "dictionaries"

/** Same thing for puzzle directory. */
var puzzleDir = project.property("datadir") as String + File.separator + "puzzles"

/**
 * Configures application parameters.
 *
 * Note that more parameters could be set here ('executableDir', 'applicationDefaultJvmArgs') but
 * in order to clearly separate parameters specific to a launch configuration, only parameters
 * common to all launch configurations are kept here, so 'mainClass' and 'mainModule'.
 */
application {
    mainClass.set("${project.group}.${project.name}.Main".replace('-', '.'))
    mainModule.set("${project.group}.${project.name}".replace('-', '.'))
    applicationDistribution.from(configurations.named("dictionaryPath")).into(dictionaryDir)
}

/**
 * Configures parameters to be used when launching the application from distributed start script.
 *
 * Note that $APP_HOME corresponds to the installation prefix. Dictionary data path is set
 * relatively to this prefix. E.g. if one wants to have binaries in '/usr/bin' and data in
 * '/usr/share/crosswords/dictionaries' then one has to set the datadir property to
 * 'share/crosswords/dictionaries' and copy the installation tree to '/usr'.
 */
tasks.named<CreateStartScripts>("startScripts") {
    executableDir = project.property("bindir") as String

    // $APP_HOME cannot be referenced directly in defaultJvmOpts (template engine escapes it).
    // See https://discuss.gradle.org/t/hack-to-pass-app-home-as-system-property-in-start-scripts-no-longer-working/42870/4.
    val appHome = "APP_HOME_PLACEHOLDER"
    val sep = File.separator
    val byConventionArgs = listOf(
        "-Dcom.gitlab.super7ramp.croiseur.dictionary.path=${appHome}${sep}${dictionaryDir}${sep}",
        "-Dcom.gitlab.super7ramp.croiseur.puzzle.path=${appHome}${sep}${puzzleDir}${sep}"
    )
    val appSpecificArgs = application.applicationDefaultJvmArgs
    defaultJvmOpts = appSpecificArgs + byConventionArgs

    doLast {
        unixScript.writeText(unixScript.readText().replace(appHome, "'\$APP_HOME'"))
        windowsScript.writeText(windowsScript.readText().replace(appHome, "%APP_HOME%"))
    }
}

/**
 * Disables distTar. Not used and time-consuming.
 */
tasks.named<Tar>("distTar") {
    enabled = false
}

/**
 * Configures parameters to be used when launch the application via './gradlew run'.
 */
afterEvaluate {
    tasks.named<JavaExec>("run") {
        val dictionaryPath = configurations.getByName("dictionaryPath").asPath
        systemProperty("com.gitlab.super7ramp.croiseur.dictionary.path", dictionaryPath)
        val tempPuzzlePath = Files.createTempDirectory("croiseur_test_repo_").toFile()
        tempPuzzlePath.deleteOnExit()
        systemProperty("com.gitlab.super7ramp.croiseur.puzzle.path", tempPuzzlePath)
    }
}
