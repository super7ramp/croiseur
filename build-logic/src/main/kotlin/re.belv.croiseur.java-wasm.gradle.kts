/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java libraries relying on wasm code.
 */

plugins {
    id("re.belv.croiseur.java-library")
    id("org.gradlex.extra-java-module-info")
}

// Hack to make version catalog works with kotlin, see https://github.com/gradle/gradle/issues/15383
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
dependencies {
    implementation(libs.extism.chicory)
    implementation(libs.jackson.core)
    implementation(libs.jackson.annotations)
    implementation(libs.jackson.databind)
}

extraJavaModuleInfo {
    module("com.dylibso.chicory:aot-experimental", "chicory.aot") {
        exports("com.dylibso.chicory.experimental.aot")
    }
    module("com.dylibso.chicory:log", "chicory.log") {
        exports("com.dylibso.chicory.log")
    }
    module("com.dylibso.chicory:runtime", "chicory.runtime") {
        exports("com.dylibso.chicory.runtime")
    }
    module("com.dylibso.chicory:wasi", "chicory.wasi") {
        exports("com.dylibso.chicory.wasi")
    }
    module("com.dylibso.chicory:wasm", "chicory.wasm") {
        exports("com.dylibso.chicory.wasm")
    }
    module("org.extism.sdk:chicory-sdk", "chicory.sdk") {
        exports("org.extism.sdk.chicory")
    }
}

// Configuration of the wasm library
configurations.register("wasm") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

tasks.processResources {
    // Copy the wasm library into the final jar
    from(configurations.named("wasm"))
}
