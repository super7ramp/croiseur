/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import java.util.*

/**
 * Conventions for Java libraries relying on JNI implemented in Rust.
 */

plugins {
    id("re.belv.croiseur.java-library")
}

// Configuration of the rust native library
configurations.register("rust") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.processResources {
    // Copy the native library into the final jar
    from(configurations.named("rust"))
}

tasks.jar {
    /*
     * As jar includes a native library, it is suffixed with the native library operating system
     * identification. It is assumed that native library is not cross-compiled, hence using current
     * system information.
     */
    archiveClassifier = currentOperatingSystemIdentifier()
}

tasks.test {
    // Some settings to ease native code debugging
    environment("RUST_BACKTRACE", "1")
    jvmArgs("-Xcheck:jni")
}

fun currentOperatingSystemIdentifier(): String {
    val osName = System.getProperty("os.name").lowercase(Locale.ENGLISH)
    val osArch = System.getProperty("os.arch").lowercase(Locale.ENGLISH)
    return "${osName}-${osArch}"
}