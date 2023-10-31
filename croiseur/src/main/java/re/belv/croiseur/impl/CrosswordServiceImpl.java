/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl;

import re.belv.croiseur.api.CrosswordService;
import re.belv.croiseur.api.clue.ClueService;
import re.belv.croiseur.api.dictionary.DictionaryService;
import re.belv.croiseur.api.puzzle.PuzzleService;
import re.belv.croiseur.api.solver.SolverService;
import re.belv.croiseur.impl.clue.ClueServiceImpl;
import re.belv.croiseur.impl.dictionary.DictionaryServiceImpl;
import re.belv.croiseur.impl.puzzle.PuzzleServiceImpl;
import re.belv.croiseur.impl.solver.SolverServiceImpl;
import re.belv.croiseur.spi.clue.ClueProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;
import re.belv.croiseur.spi.presenter.Presenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;
import re.belv.croiseur.spi.solver.CrosswordSolver;

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

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /**
     * Constructor.
     *
     * @param dictionaryProviders the dictionary providers
     * @param solvers             the solvers
     * @param clueProviders       the clue providers
     * @param puzzleDecoders      the puzzle decoders
     * @param puzzleEncoders      the puzzle encoders
     * @param puzzleRepository    the puzzle repository
     * @param presenter           the publisher
     */
    public CrosswordServiceImpl(final Collection<DictionaryProvider> dictionaryProviders,
                                final Collection<CrosswordSolver> solvers,
                                final Collection<ClueProvider> clueProviders,
                                final Collection<PuzzleDecoder> puzzleDecoders,
                                final Collection<PuzzleEncoder> puzzleEncoders,
                                final PuzzleRepository puzzleRepository,
                                final Presenter presenter) {
        dictionaryService = new DictionaryServiceImpl(dictionaryProviders, presenter);
        solverService =
                new SolverServiceImpl(solvers, dictionaryProviders, clueProviders, puzzleRepository,
                                      presenter);
        clueService = new ClueServiceImpl(clueProviders, presenter);
        puzzleService =
                new PuzzleServiceImpl(puzzleRepository, puzzleDecoders, puzzleEncoders, presenter);
    }

    @Override
    public DictionaryService dictionaryService() {
        return dictionaryService;
    }

    @Override
    public SolverService solverService() {
        return solverService;
    }

    @Override
    public ClueService clueService() {
        return clueService;
    }

    @Override
    public PuzzleService puzzleService() {
        return puzzleService;
    }
}
