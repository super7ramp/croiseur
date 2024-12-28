/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    `kotlin-dsl`
}

dependencies {
    // Hack to make sbom accessible in convention plugin bodies (gh#gradle/gradle#15383)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.jmh.plugin)
    implementation(libs.native)
    implementation(libs.spotless)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
