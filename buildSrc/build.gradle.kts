/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    `kotlin-dsl`
}

dependencies {
    // Hack to make sbom accessible in convention plugin bodies (gh#gradle/gradle#15383)
    implementation(files(sbom.javaClass.superclass.protectionDomain.codeSource.location))
    // Explicit dependency required in plugin block of convention plugin
    implementation(sbom.native)
}

repositories {
    mavenCentral()
}