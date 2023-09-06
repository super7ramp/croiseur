/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import java.nio.file.Files

/**
 * Conventions for Java applications.
 */

plugins {
    id("re.belv.croiseur.java")
    id("application")
}

/**
 * The resolvable dictionary path, where the dictionaries come from.
 */
configurations.register("dictionaryPath") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

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
    applicationDistribution.from(configurations.named("dictionaryPath")).into(dictionaryDistDir())
}

/**
 * Configures parameters to be used when launching the application from distributed start script.
 *
 * Note that $APP_HOME corresponds to the installation prefix. Dictionary data path is set
 * relatively to this prefix. E.g. if one wants to have binaries in '/usr/bin' and data in
 * '/usr/share/crosswords/dictionaries' then one has to set the datadir property to
 * 'share/crosswords/dictionaries' and copy the installation tree to '/usr'.
 */
tasks.startScripts {
    executableDir = binaryDistDir()

    // $APP_HOME cannot be referenced directly in defaultJvmOpts (template engine escapes it).
    // See https://discuss.gradle.org/t/hack-to-pass-app-home-as-system-property-in-start-scripts-no-longer-working/42870/4.
    val appHome = "APP_HOME_PLACEHOLDER"
    val sep = File.separator
    val byConventionArgs = listOf(
        "-Dre.belv.croiseur.dictionary.path=${appHome}${sep}${dictionaryDistDir()}${sep}",
        "-Dre.belv.croiseur.puzzle.path=${appHome}${sep}${puzzleDistDir()}${sep}"
    )
    val appSpecificArgs = application.applicationDefaultJvmArgs
    defaultJvmOpts = appSpecificArgs + byConventionArgs

    doLast {
        unixScript.writeText(unixScript.readText().replace(appHome, "'\$APP_HOME'"))
        windowsScript.writeText(windowsScript.readText().replace(appHome, "%APP_HOME%"))
    }
}

/** Disables distTar. Not used and time-consuming. */
tasks.distTar {
    enabled = false
}

/** Configures parameters to be used when launch the application via './gradlew run'. */
tasks.named<JavaExec>("run") {
    systemProperty("re.belv.croiseur.dictionary.path", resolvedDicPath())
    systemProperty("re.belv.croiseur.puzzle.path", testPuzzlePath())
}

/** Configures parameters to be used when launch the application via './gradlew bootRun'. */
pluginManager.withPlugin("org.springframework.boot") {
    tasks.named<JavaExec>("bootRun") {
        systemProperty("re.belv.croiseur.dictionary.path", resolvedDicPath())
        systemProperty("re.belv.croiseur.puzzle.path", testPuzzlePath())
    }
}

/** Returns the binary directory, relative to destination directory. */
fun binaryDistDir(): String {
    return project.property("bindir") as String
}

/** Returns the puzzle distribution directory, relative to destination directory. */
fun puzzleDistDir(): String {
    return project.property("datadir") as String + File.separator + "puzzles"
}

/** Returns the dictionary distribution directory, relative to destination directory. */
fun dictionaryDistDir(): String {
    return project.property("datadir") as String + File.separator + "dictionaries"
}

fun resolvedDicPath(): String {
    return configurations.getByName("dictionaryPath").asPath
}

fun testPuzzlePath(): String {
    val tempDir = Files.createTempDirectory("croiseur_test_repo_").toFile()
    tempDir.deleteOnExit()
    return tempDir.path
}