/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.jni-rust-conventions")
}

dependencies {
    rust(project(":croiseur-solver:croiseur-solver-paulgb:crossword-composer-jni"))
}