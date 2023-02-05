/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.impl.clue.ClueServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.dictionary.DictionaryServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.solver.SolverServiceImpl;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

import java.util.Collection;

/**
 * Implementation of {@link CrosswordService}.
 */
public final class CrosswordServiceImpl implements CrosswordService {

    /** Dictionary service. */
    private final DictionaryService dictionaryService;

    /** Solver service. */
    private final SolverService solverService;

    /** Clue service. */
    private final ClueService clueService;

    /**
     * Constructor.
     *
     * @param dictionaryProviders the dictionary providers
     * @param solvers             the solvers
     * @param clueProviders       the clue providers
     * @param presenter           the publisher
     */
    public CrosswordServiceImpl(final Collection<DictionaryProvider> dictionaryProviders,
                                final Collection<CrosswordSolver> solvers,
                                final Collection<ClueProvider> clueProviders,
                                final Presenter presenter) {
        solverService = new SolverServiceImpl(solvers, dictionaryProviders, presenter);
        dictionaryService = new DictionaryServiceImpl(dictionaryProviders, presenter);
        clueService = new ClueServiceImpl(clueProviders, presenter);
    }

    @Override
    public DictionaryService dictionaryService() {
        return dictionaryService;
    }

    @Override
    public ClueService clueService() {
        return clueService;
    }

    @Override
    public SolverService solverService() {
        return solverService;
    }
}
