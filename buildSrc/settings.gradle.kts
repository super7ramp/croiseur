/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

rootProject.name = "gradle-conventions"

// Dependency versions
dependencyResolutionManagement {
    versionCatalogs {
        create("sbom") {
            from(files("../sbom.toml"))
        }
    }
}