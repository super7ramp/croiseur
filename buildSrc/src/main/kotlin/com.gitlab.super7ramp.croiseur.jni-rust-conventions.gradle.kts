/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java libraries relying on JNI implemented in Rust.
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
}

// Configuration of the native libraries (input)
configurations.register("rust") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.named<ProcessResources>("processResources") {
    from(configurations.named("rust")) {
        include("*.dll")
        into("native/windows-amd64/")
    }
    from(configurations.named("rust")) {
        include("*.so")
        into("native/linux-amd64/")
    }
}

tasks.named<Test>("test") {
    // Some settings to ease native code debugging
    environment("RUST_BACKTRACE", "1")
    jvmArgs("-Xcheck:jni")
}
