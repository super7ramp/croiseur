/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("re.belv.croiseur.java-library")
    alias(sbom.plugins.pitest)
}

pitest {
    junit5PluginVersion = sbom.versions.pitest.junit5
    verbose = true
}