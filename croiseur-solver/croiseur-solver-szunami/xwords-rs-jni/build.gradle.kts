/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.cargo.CargoTask

plugins {
    id("com.gitlab.super7ramp.croiseur.cargo-wrapper")
}

cargo {
    outputs = mapOf(
        /* The default target and its output. */
        "" to System.mapLibraryName("xwords_rs_jni"),
        /*
         * Below are all supported targets and their output. They are not compiled by the default
         * CargoTask, but they can be performed with a CargoTask with custom arguments.
         */
        "x86_64-unknown-linux-gnu" to "libxwords_rs_jni.so",
        "x86_64-pc-windows-gnu" to "xwords_rs_jni.dll"
    )
    profile = "release"
}

tasks.register<CargoTask>("crossBuild") {
    description = "Cross-compile the library against all main targets (external linkers required)"
    args = listOf(
        "--target", "x86_64-unknown-linux-gnu",
        "--target", "x86_64-pc-windows-gnu"
    )
}