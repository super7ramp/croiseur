/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api;

import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.impl.CrosswordServiceImpl;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * Crossword services.
 */
public interface CrosswordService {

    /**
     * Creates a new instance of {@link CrosswordService}.
     * <p>
     * Required services are explicitly passed as arguments.
     *
     * @param dictionaryProviders the dictionary providers
     * @param clueProviders       the clue providers
     * @param solvers             the solver
     * @param presenter           the presenter
     * @return a new instance of {@link CrosswordService}
     */
    static CrosswordService create(final Collection<DictionaryProvider> dictionaryProviders,
                                   final Collection<CrosswordSolver> solvers,
                                   final Collection<ClueProvider> clueProviders,
                                   final Presenter presenter) {
        return new CrosswordServiceImpl(dictionaryProviders, solvers, clueProviders, presenter);
    }

    /**
     * Creates a new instance of {@link CrosswordService}.
     * <p>
     * Required services are automatically loaded using {@link ServiceLoader}.
     *
     * @return a new instance of {@link CrosswordService}
     * @throws IllegalStateException if some required services cannot be found
     */
    static CrosswordService create() {
        final Collection<DictionaryProvider> dictionaryProviders =
                ServiceLoader.load(DictionaryProvider.class).stream()
                             .map(Supplier::get)
                             .toList();
        final Collection<CrosswordSolver> solvers =
                ServiceLoader.load(CrosswordSolver.class)
                             .stream()
                             .map(Supplier::get)
                             .toList();
        final Collection<ClueProvider> clueProviders =
                ServiceLoader.load(ClueProvider.class)
                             .stream()
                             .map(Supplier::get)
                             .toList();
        final Presenter presenter =
                ServiceLoader.load(Presenter.class)
                             .findFirst()
                             .orElseThrow(() -> new IllegalStateException(
                                     "Failed to instantiate crosswords service: No presenter " +
                                             "found"));
        return create(dictionaryProviders, solvers, clueProviders, presenter);
    }

    /**
     * Returns the dictionary service.
     *
     * @return the dictionary service
     */
    DictionaryService dictionaryService();

    /**
     * Returns the clue service.
     *
     * @return the clue service
     */
    ClueService clueService();

    /**
     * Returns the solver service.
     *
     * @return the solver service
     */
    SolverService solverService();

}
