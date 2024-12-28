/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java benchmarks.
 */

plugins {
    id("re.belv.croiseur.java")
    // Cannot use version catalog in plugins block of pre-compiled script plugins, i.e.
    // "alias(sbom.plugins.jmh)" doesn't work (gh#gradle/gradle#15383)
    id("me.champeau.jmh")
}

jmh {
    resultFormat = "json"
}