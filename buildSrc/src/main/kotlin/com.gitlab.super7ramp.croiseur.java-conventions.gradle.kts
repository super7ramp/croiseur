/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java.
 */

plugins {
    id("java")
}

group = "com.gitlab.super7ramp"

repositories {
    mavenCentral()
}

// Hack to make version catalog works with kotlin, see:
// - https://github.com/gradle/gradle/issues/15383
// - https://github.com/gradle/gradle/issues/22468
if (!project.name.equals("gradle-kotlin-dsl-accessors")) {
    val sbom = the<org.gradle.accessors.dm.LibrariesForSbom>()
    dependencies {
        testImplementation(sbom.junit5.api)
        testRuntimeOnly(sbom.junit5.engine)
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
