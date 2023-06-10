/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

/**
 * Application core library.
 * <p>
 * This module is a library providing several high-level crossword puzzle use-cases.
 */
module com.gitlab.super7ramp.croiseur {

    /*
     * Requires plugins.
     *
     * Transitive since plugin implementations can be explicitly passed in factory and hence are
     * visible from client API.
     */
    requires transitive com.gitlab.super7ramp.croiseur.spi.dictionary;
    requires transitive com.gitlab.super7ramp.croiseur.spi.puzzle.repository;
    requires transitive com.gitlab.super7ramp.croiseur.spi.solver;
    requires transitive com.gitlab.super7ramp.croiseur.spi.presenter;

    // Exports only API, keeps implementation hidden.
    exports com.gitlab.super7ramp.croiseur.api;
    exports com.gitlab.super7ramp.croiseur.api.dictionary;
    exports com.gitlab.super7ramp.croiseur.api.solver;

    // Uses plugins since plugin implementations can be implicitly loaded in factory.
    uses DictionaryProvider;
    uses CrosswordSolver;
    uses Presenter;
}