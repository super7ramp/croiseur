/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.impl.CrosswordServiceImpl;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.DummyPuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
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
     * @param solvers             the solvers
     * @param puzzleRepository    the puzzle repository; If puzzle repository service is not going
     *                            to be used, then {@link DummyPuzzleRepository} can be passed here
     * @param presenter           the presenter
     * @return a new instance of {@link CrosswordService}
     */
    static CrosswordService create(final Collection<DictionaryProvider> dictionaryProviders,
                                   final Collection<CrosswordSolver> solvers,
                                   final PuzzleRepository puzzleRepository,
                                   final Presenter presenter) {
        return new CrosswordServiceImpl(solvers, dictionaryProviders, puzzleRepository, presenter);
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
        final Collection<DictionaryProvider> dictionaryProviders = load(DictionaryProvider.class);
        final Collection<CrosswordSolver> solvers = load(CrosswordSolver.class);
        final PuzzleRepository puzzleRepository =
                load(PuzzleRepository.class).stream().findFirst().orElseGet(
                        DummyPuzzleRepository::new);
        final Collection<Presenter> presenters = load(Presenter.class);
        if (presenters.isEmpty()) {
            throw new IllegalStateException(
                    "Failed to instantiate crossword service: No presenter found");
        }
        return create(dictionaryProviders, solvers, puzzleRepository,
                      Presenter.broadcastingTo(presenters));
    }

    /**
     * Loads all the implementations of the given service class.
     *
     * @param clazz the service provider class
     * @param <T>   the type of the service
     * @return all the implementations of the given service class
     */
    private static <T> Collection<T> load(final Class<T> clazz) {
        return ServiceLoader.load(clazz).stream().map(Supplier::get).toList();
    }

    /**
     * Returns the dictionary service.
     *
     * @return the dictionary service
     */
    DictionaryService dictionaryService();

    /**
     * Returns the solver service.
     *
     * @return the solver service
     */
    SolverService solverService();

}
