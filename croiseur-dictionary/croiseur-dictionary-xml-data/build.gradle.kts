/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.dictionary-data-conventions")
}

fileTree(layout.projectDirectory) { exclude("build*", "*.md") }.forEach {
    artifacts.add("dictionary", it)
}