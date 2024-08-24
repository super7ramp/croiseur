/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.solver.CrosswordSolver;

/**
 * Application core library.
 * <p>
 * This module is a library providing several high-level crossword puzzle use-cases.
 */
module re.belv.croiseur {

    /*
     * Requires plugins.
     *
     * Transitive since plugin implementations can be explicitly passed in factory and hence are
     * visible from client API.
     */
    requires transitive re.belv.croiseur.spi.clue;
    requires transitive re.belv.croiseur.spi.dictionary;
    requires transitive re.belv.croiseur.spi.presenter;
    requires transitive re.belv.croiseur.spi.puzzle.codec;
    requires transitive re.belv.croiseur.spi.puzzle.repository;
    requires transitive re.belv.croiseur.spi.solver;

    // Uses plugins since plugins can be implicitly loaded in factory.
    uses ClueProvider;
    uses CrosswordSolver;
    uses DictionaryProvider;
    uses Presenter;
    uses PuzzleDecoder;
    uses PuzzleEncoder;
    uses PuzzleRepository;

    // Exports only API, keeps implementation hidden.
    exports re.belv.croiseur.api;
    exports re.belv.croiseur.api.clue;
    exports re.belv.croiseur.api.dictionary;
    exports re.belv.croiseur.api.puzzle;
    exports re.belv.croiseur.api.puzzle.exporter;
    exports re.belv.croiseur.api.puzzle.importer;
    exports re.belv.croiseur.api.puzzle.persistence;
    exports re.belv.croiseur.api.solver;
}
