/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    alias(libs.plugins.cargo)
}

cargo {
    arguments = listOf("--verbose", "--target", "wasm32-unknown-unknown")
    outputs = mapOf("wasm32-unknown-unknown" to "xwords_rs_wasm.wasm")
    profile = "release"
}
