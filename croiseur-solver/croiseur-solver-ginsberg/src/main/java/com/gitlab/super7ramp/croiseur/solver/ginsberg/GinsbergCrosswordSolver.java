/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.Backtracker;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.CandidateChooser;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.Solver;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.backtrack.Backtrackers;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.instantiation.CandidateChoosers;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.iteration.SlotIteratorImpl;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.listener.ProgressDebugPrinter;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.listener.ProgressNotifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.listener.StatisticsRecorder;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Prober;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.result.SolverResultFactory;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.state.Crossword;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.state.CrosswordUpdater;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * A crossword solver.
 */
public final class GinsbergCrosswordSolver {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GinsbergCrosswordSolver.class.getName());

    /**
     * Constructor.
     */
    public GinsbergCrosswordSolver() {
        // Nothing to do.
    }

    /**
     * Creates new internal solver.
     *
     * @param problem            the solver internal state
     * @param progressListener   a progress listener
     * @param statisticsRecorder another listener for stats
     * @return the created solver
     */
    private static Solver newSolver(final Crossword problem,
                                    final ProgressListener progressListener,
                                    final StatisticsRecorder statisticsRecorder) {


        final Collection<Slot> slots = problem.grid().puzzle().slots();

        // Instantiates heuristics
        final SlotIteratorImpl slotChooser = new SlotIteratorImpl(slots, problem.dictionary());
        final CandidateChooser<Slot, String> candidateChooser =
                CandidateChoosers.byDefault(problem.grid().puzzle(), problem.dictionary());
        final Backtracker<Slot, SlotIdentifier> backtracker =
                Backtrackers.byDefault(new Prober(problem.grid()
                                                         .puzzle(), problem.dictionary()),
                        problem.grid().puzzle(),
                        problem.history());

        // A listener to advertise progress to library user
        final ProgressNotifier progressNotifier = new ProgressNotifier(slots, progressListener);

        // A listener to advertise progress to library developer
        final ProgressDebugPrinter progressDebugPrinter = new ProgressDebugPrinter(problem.grid());

        // The internal state updater
        final CrosswordUpdater crosswordUpdater =
                new CrosswordUpdater(problem).withListeners(progressNotifier, statisticsRecorder,
                        progressDebugPrinter);

        // Finally, instantiate the solver
        return Solver.create(crosswordUpdater, slotChooser, candidateChooser, backtracker);
    }

    /**
     * Solve the given puzzle, using the given dictionary.
     *
     * @param puzzleDefinition   the puzzle to solve
     * @param externalDictionary the dictionary to use
     * @param progressListener   the progress listener
     * @return the result
     * @throws InterruptedException if interrupted while solving
     */
    public SolverResult solve(final PuzzleDefinition puzzleDefinition,
                              final Dictionary externalDictionary,
                              final ProgressListener progressListener) throws InterruptedException {

        progressListener.onInitialisationStart();

        final Crossword crossword = Crossword.create(puzzleDefinition, externalDictionary);
        final StatisticsRecorder stats = new StatisticsRecorder();
        final Solver solver = newSolver(crossword, progressListener, stats);

        LOGGER.info(() -> "Total branches (box variables): " + Math.pow(26,
                puzzleDefinition.height() * puzzleDefinition.width()));
        final BigInteger branches = crossword.grid()
                                             .puzzle()
                                             .slots()
                                             .stream()
                                             .map(s -> BigInteger.valueOf(crossword.dictionary()
                                                                                   .candidatesCount(s)))
                                             .reduce(BigInteger.ONE, BigInteger::multiply);
        final NumberFormat formatter = new DecimalFormat("0.######E0",
                DecimalFormatSymbols.getInstance(Locale.ROOT));
        LOGGER.info(() -> "Total branches (slot variables, pruned): " + formatter.format(branches));

        progressListener.onInitialisationEnd();

        final boolean solved = solver.solve();

        LOGGER.info(() -> "Dictionary cache dump after resolution: ");
        final StringBuilder sb = new StringBuilder();
        for (final Slot slot : crossword.grid().puzzle().slots()) {
            sb.append(slot).append(": ").append(crossword.dictionary().candidates(slot).toList());
            sb.append(System.lineSeparator());
        }
        LOGGER.info(sb::toString);
        LOGGER.info(() -> "Elimination space dump after resolution: ");
        LOGGER.info(() -> crossword.eliminationSpace().toString());

        return SolverResultFactory.createFrom(crossword, stats, solved);
    }

    /**
     * Solve the given puzzle, using the given dictionary.
     *
     * @param puzzle     the puzzle to solve
     * @param dictionary the dictionary to use
     * @return the result
     * @throws InterruptedException if interrupted while solving
     */
    public SolverResult solve(final PuzzleDefinition puzzle, final Dictionary dictionary) throws InterruptedException {
        return solve(puzzle, dictionary, ProgressListener.DUMMY_LISTENER);
    }

}
