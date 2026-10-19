/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/** Acceptance tests of the core library and its main solver and dictionary plugins. */
open module re.belv.croiseur.tests {
    // the core library under tests
    requires re.belv.croiseur;

    // test frameworks
    requires io.cucumber.java;
    requires org.junit.jupiter.api;
    requires org.junit.platform.suite.api;
    requires org.mockito;

    // module loads plugins itself
    uses ClueProvider;
    uses CrosswordSolver;
    uses DictionaryProvider;
    uses PuzzleDecoder;
    uses PuzzleEncoder;
    uses PuzzleRepository;
}
