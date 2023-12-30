/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java.
 */

plugins {
    id("re.belv.croiseur.base")
    java
    jacoco
}

// Hack to make version catalog works with kotlin, see https://github.com/gradle/gradle/issues/15383
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
dependencies {
    testImplementation(libs.junit5.api)
    testRuntimeOnly(libs.junit5.engine)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

tasks.withType(JavaCompile::class).configureEach {
    options.encoding = "UTF-8"
}

tasks.withType(Javadoc::class).configureEach {
    options.encoding = "UTF-8"
}

tasks.withType(Test::class).configureEach {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    // Do not generate reports for individual projects by default.
    enabled = false
}
