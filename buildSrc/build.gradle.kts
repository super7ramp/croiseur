/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    // Hack to make sbom accessible in convention plugin bodies (gh#gradle/gradle#15383)
    implementation(files(sbom.javaClass.superclass.protectionDomain.codeSource.location))
    // Explicit dependency required in plugin block of convention plugin
    implementation(sbom.native)
    implementation(sbom.extra.java.module.info)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

// This forces compilation of buildSrc with target Java 17, until Kotlin compiler supports Java 21
// See https://github.com/gradle/gradle/issues/26543.
tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<JavaCompile>().configureEach {
    targetCompatibility = "17"
}