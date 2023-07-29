/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java libraries consuming native methods through JNI.
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
}

// Configuration of the native libraries (input)
configurations.register("nativeLibrary") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.named<ProcessResources>("processResources") {
    from(configurations.named("nativeLibrary")) {
        include("*.dll")
        into("native/windows-amd64/")
    }
    from(configurations.named("nativeLibrary")) {
        include("*.so")
        into("native/linux-amd64/")
    }
}

tasks.named<Test>("test") {
    // Some settings to ease native code debugging
    environment("RUST_BACKTRACE", "1") // Only useful for Rust binaries, harmless for the others
    jvmArgs("-Xcheck:jni")
}
