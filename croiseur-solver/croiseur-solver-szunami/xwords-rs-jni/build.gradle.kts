/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    alias(sbom.plugins.cargo)
}

cargo {
    outputs = mapOf("" to System.mapLibraryName("xwords_rs_jni"))
    profile = "release"
}