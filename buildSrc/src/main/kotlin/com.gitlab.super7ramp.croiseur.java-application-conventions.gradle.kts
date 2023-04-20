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

configurations.create("dictionaryPath") {
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
}

val dictionaryDir = project.property("datadir").toString() + File.separator + "dictionaries"

application.applicationDistribution.from(configurations.getByName("dictionaryPath")) {
    into(dictionaryDir)
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
    executableDir = project.property("bindir").toString()

    // $APP_HOME cannot be referenced directly in defaultJvmOpts (template engine escapes it).
    // See https://discuss.gradle.org/t/hack-to-pass-app-home-as-system-property-in-start-scripts-no-longer-working/42870/4.
    val appHome = "APP_HOME_PLACEHOLDER"
    val sep = File.separator
    defaultJvmOpts = listOf("-Dcom.gitlab.super7ramp.croiseur.dictionary." +
                              "path=${appHome}${sep}${dictionaryDir}${sep}".toString())

    doLast {
        // TODO scripts are of type File, no easy way to replace all text in Kotlin
        //unixScript.text(unixScript.text().replace(appHome, "'\$APP_HOME'"))
        //windowsScript.text(windowsScript.text.replace(appHome, "%APP_HOME%"))
    }
}

/*
 * Disable disTar. Not used and time-consuming.
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
    }
}
