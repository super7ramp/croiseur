/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.impl.clue.ClueServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.dictionary.DictionaryServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.puzzle.PuzzleServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.solver.SolverServiceImpl;
import com.gitlab.super7ramp.croiseur.spi.clue.ClueProvider;
import com.gitlab.super7ramp.croiseur.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

import java.util.Collection;

/**
 * Implementation of {@link CrosswordService}.
 */
public final class CrosswordServiceImpl implements CrosswordService {

    /** Dictionary service. */
    private final DictionaryService dictionaryService;

    /** Clue service. */
    private final ClueService clueService;

    /** Solver service. */
    private final SolverService solverService;

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /**
     * Constructor.
     *
     * @param solvers             the solvers
     * @param dictionaryProviders the dictionary providers
     * @param clueProviders       the clue providers
     * @param puzzleDecoders      the puzzle decoders
     * @param puzzleEncoders      the puzzle encoders
     * @param puzzleRepository    the puzzle repository
     * @param presenter           the publisher
     */
    public CrosswordServiceImpl(final Collection<CrosswordSolver> solvers,
                                final Collection<DictionaryProvider> dictionaryProviders,
                                final Collection<ClueProvider> clueProviders,
                                final Collection<PuzzleDecoder> puzzleDecoders,
                                final Collection<PuzzleEncoder> puzzleEncoders,
                                final PuzzleRepository puzzleRepository,
                                final Presenter presenter) {
        solverService =
                new SolverServiceImpl(solvers, dictionaryProviders, puzzleRepository, presenter);
        dictionaryService = new DictionaryServiceImpl(dictionaryProviders, presenter);
        clueService = new ClueServiceImpl(clueProviders, presenter);
        puzzleService =
                new PuzzleServiceImpl(puzzleRepository, puzzleDecoders, puzzleEncoders, presenter);
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

    @Override
    public PuzzleService puzzleService() {
        return puzzleService;
    }
}
