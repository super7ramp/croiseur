/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java.
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.base-conventions")
    // Cannot use version catalog in plugins block of pre-compiled script plugins, i.e.
    // "alias(sbom.plugins.extra.java.module.info)" doesn't work (gh#gradle/gradle#15383)
    id("org.gradlex.extra-java-module-info")
    java
    jacoco
}

// Hack to make version catalog works with kotlin, see https://github.com/gradle/gradle/issues/15383
val sbom = the<org.gradle.accessors.dm.LibrariesForSbom>()
dependencies {
    testImplementation(sbom.junit5.api)
    testRuntimeOnly(sbom.junit5.engine)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
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

tasks.named<JacocoReport>("jacocoTestReport") {
    // Do not generate reports for individual projects by default.
    enabled = false
}
