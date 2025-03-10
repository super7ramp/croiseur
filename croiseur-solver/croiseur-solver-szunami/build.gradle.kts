/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-wasm")
}

dependencies {
    wasm(project(":croiseur-solver:croiseur-solver-szunami:xwords-rs-wasm"))
}

tasks.test {
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}
