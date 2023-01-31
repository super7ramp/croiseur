/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.impl.dictionary.DictionaryServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.solver.SolverServiceImpl;
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

    /**
     * Constructor.
     *
     * @param solvers             the solvers
     * @param dictionaryProviders the dictionary providers
     * @param presenter           the publisher
     */
    public CrosswordServiceImpl(final Collection<CrosswordSolver> solvers,
                                final Collection<DictionaryProvider> dictionaryProviders,
                                final Presenter presenter) {
        solverService = new SolverServiceImpl(solvers, dictionaryProviders, presenter);
        dictionaryService = new DictionaryServiceImpl(dictionaryProviders, presenter);
    }

    @Override
    public DictionaryService dictionaryService() {
        return dictionaryService;
    }

    @Override
    public SolverService solverService() {
        return solverService;
    }
}
