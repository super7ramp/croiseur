/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java.
 */

plugins {
    id("re.belv.croiseur.base")
    id("com.diffplug.spotless")
    java
    jacoco
}

// Hack to make version catalog works with kotlin, see https://github.com/gradle/gradle/issues/15383
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

spotless {
    java {
        palantirJavaFormat().formatJavadoc(true)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    useJUnitPlatform()
    reports {
        html.required = false
    }
}

tasks.jacocoTestReport {
    // Do not generate reports for individual projects by default.
    enabled = false
}
