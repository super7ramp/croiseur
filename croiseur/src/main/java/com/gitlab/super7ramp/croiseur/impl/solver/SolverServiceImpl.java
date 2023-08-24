/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.solver;

import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

import java.util.Collection;

/**
 * Implementation of {@link SolverService}.
 */
public final class SolverServiceImpl implements SolverService {

    /** The 'list-solver' usecase. */
    private final ListSolversUsecase listSolversUsecase;

    /** The 'solve' usecase. */
    private final SolveUsecase solveUsecase;

    /**
     * Constructs an instance.
     *
     * @param solvers             the solvers
     * @param dictionaryProviders the dictionary providers
     * @param clueProviders       the clue providers
     * @param puzzleRepository    the puzzle repository
     * @param presenter           the solver presenter
     * @throws IllegalArgumentException if solver collection is empty
     */
    public SolverServiceImpl(final Collection<CrosswordSolver> solvers,
                             final Collection<DictionaryProvider> dictionaryProviders,
                             final Collection<ClueProvider> clueProviders,
                             final PuzzleRepository puzzleRepository,
                             final Presenter presenter) {
        listSolversUsecase = new ListSolversUsecase(solvers, presenter);
        solveUsecase = new SolveUsecase(solvers, dictionaryProviders, clueProviders,
                                        puzzleRepository, presenter);
    }

    @Override
    public void listSolvers() {
        listSolversUsecase.process();
    }

    @Override
    public void solve(final SolveRequest event) {
        solveUsecase.process(event);
    }

}
