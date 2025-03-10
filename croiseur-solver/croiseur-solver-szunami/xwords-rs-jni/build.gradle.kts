/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    alias(libs.plugins.cargo)
}

cargo {
    cargoCommand = "/Users/57139H/.cargo/bin/cargo"
    arguments = listOf("--verbose")
    outputs = mapOf("" to System.mapLibraryName("xwords_rs_jni"))
    profile = "release"
}
