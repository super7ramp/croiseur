/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

rootProject.name = "croiseur"

// The project's Gradle plugins
pluginManagement {
    includeBuild("build-logic")
}

// The application core library
include("croiseur")

// The common types used as building blocks by all other modules
include("croiseur-common")

// The application core library plugin definitions
include("croiseur-spi:croiseur-spi-clue")
include("croiseur-spi:croiseur-spi-dictionary")
include("croiseur-spi:croiseur-spi-presenter")
include("croiseur-spi:croiseur-spi-puzzle-codec")
include("croiseur-spi:croiseur-spi-puzzle-repository")
include("croiseur-spi:croiseur-spi-solver")

// Dictionary codecs/data/plugins/tools
include("croiseur-dictionary:croiseur-dictionary-common")
include("croiseur-dictionary:croiseur-dictionary-example-plugin")
include("croiseur-dictionary:croiseur-dictionary-hunspell-codec")
include("croiseur-dictionary:croiseur-dictionary-hunspell-data")
include("croiseur-dictionary:croiseur-dictionary-hunspell-plugin")
include("croiseur-dictionary:croiseur-dictionary-tools")
include("croiseur-dictionary:croiseur-dictionary-txt-data")
include("croiseur-dictionary:croiseur-dictionary-txt-plugin")
include("croiseur-dictionary:croiseur-dictionary-xml-codec")
include("croiseur-dictionary:croiseur-dictionary-xml-data")
include("croiseur-dictionary:croiseur-dictionary-xml-plugin")

// Solvers libraries/plugins
include("croiseur-solver:croiseur-solver-benchmark")
include("croiseur-solver:croiseur-solver-example-plugin")
include("croiseur-solver:croiseur-solver-ginsberg")
include("croiseur-solver:croiseur-solver-ginsberg-plugin")
include("croiseur-solver:croiseur-solver-paulgb")
include("croiseur-solver:croiseur-solver-paulgb:crossword-composer-jni")
include("croiseur-solver:croiseur-solver-paulgb-plugin")
include("croiseur-solver:croiseur-solver-sat")
include("croiseur-solver:croiseur-solver-sat-plugin")
include("croiseur-solver:croiseur-solver-szunami")
include("croiseur-solver:croiseur-solver-szunami:xwords-rs-jni")
include("croiseur-solver:croiseur-solver-szunami:xwords-rs-wasm")
include("croiseur-solver:croiseur-solver-szunami-plugin")

// Clue libraries/plugins
include("croiseur-clue:croiseur-clue-openai-plugin")

// Puzzles codecs/data/providers
include("croiseur-puzzle:croiseur-puzzle-codec-xd")
include("croiseur-puzzle:croiseur-puzzle-codec-xd-plugin")
include("croiseur-puzzle:croiseur-puzzle-repository-filesystem-plugin")
include("croiseur-puzzle:croiseur-puzzle-repository-memory-plugin")

// CLI frontend
include("croiseur-cli:croiseur-cli")
include("croiseur-cli:croiseur-cli-tests")

// GUI frontend
include("croiseur-gui:croiseur-gui")
include("croiseur-gui:croiseur-gui-controller")
include("croiseur-gui:croiseur-gui-presenter")
include("croiseur-gui:croiseur-gui-view")
include("croiseur-gui:croiseur-gui-view-model")
include("croiseur-gui:croiseur-gui-tests")

// Acceptance tests
include("croiseur-tests")
include("croiseur-tests-coverage")
