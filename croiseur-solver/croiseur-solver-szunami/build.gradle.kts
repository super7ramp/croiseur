/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.jni-rust")
}

dependencies {
    rust(project(":croiseur-solver:croiseur-solver-szunami:xwords-rs-jni"))
}

tasks.test {
    jvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}
