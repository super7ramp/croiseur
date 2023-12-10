/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for Java Ahead-Of-Time compilation.
 */

plugins {
    id("re.belv.croiseur.java")
    // Cannot use version catalog in plugins block of pre-compiled script plugins, i.e.
    // "alias(sbom.plugins.native)" doesn't work (gh#gradle/gradle#15383)
    id("org.graalvm.buildtools.native")
}


graalvmNative {
    binaries {
        named("main") {
            // All locales for which Croiseur has translations should be included
            buildArgs.add("-H:IncludeLocales=en,fr")
            buildArgs.add("-H:+AddAllCharsets")
            verbose = true
        }
        named("test") {
            buildArgs.add("-H:IncludeLocales=en,fr")
            buildArgs.add("-H:+AddAllCharsets")
            quickBuild = true
            verbose = true
        }
    }
    metadataRepository {
        enabled = true
    }
}
