/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Logger;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;
import re.belv.croiseur.solver.ginsberg.core.sap.Backtracker;
import re.belv.croiseur.solver.ginsberg.core.sap.CandidateChooser;
import re.belv.croiseur.solver.ginsberg.core.sap.Solver;
import re.belv.croiseur.solver.ginsberg.dictionary.CachedDictionary;
import re.belv.croiseur.solver.ginsberg.heuristics.backtrack.Backtrackers;
import re.belv.croiseur.solver.ginsberg.heuristics.instantiation.CandidateChoosers;
import re.belv.croiseur.solver.ginsberg.heuristics.iteration.SlotIteratorImpl;
import re.belv.croiseur.solver.ginsberg.listener.FineProgressPrinter;
import re.belv.croiseur.solver.ginsberg.listener.ProgressNotifier;
import re.belv.croiseur.solver.ginsberg.listener.StatisticsRecorder;
import re.belv.croiseur.solver.ginsberg.result.SolverResultFactory;
import re.belv.croiseur.solver.ginsberg.state.Crossword;
import re.belv.croiseur.solver.ginsberg.state.CrosswordUpdater;

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
    private static Solver newSolver(
            final Crossword problem,
            final ProgressListener progressListener,
            final StatisticsRecorder statisticsRecorder) {

        final Collection<Slot> slots = problem.grid().puzzle().slots();

        // Instantiates heuristics
        final SlotIteratorImpl slotChooser = new SlotIteratorImpl(slots, problem.dictionary());
        final CandidateChooser<Slot, String> candidateChooser =
                CandidateChoosers.byDefault(problem.probePuzzle(), problem.dictionary());
        final Backtracker<Slot, SlotIdentifier> backtracker =
                Backtrackers.byDefault(problem.grid().puzzle(), problem.probePuzzle(), problem.history());

        // A listener to advertise progress to library user
        final ProgressNotifier progressNotifier = new ProgressNotifier(slots, progressListener);

        // A listener to advertise progress to library developer
        final FineProgressPrinter fineProgressPrinter = new FineProgressPrinter(problem.grid());

        // The internal state updater
        final CrosswordUpdater crosswordUpdater =
                new CrosswordUpdater(problem).withListeners(progressNotifier, statisticsRecorder, fineProgressPrinter);

        // Finally, instantiate the solver
        return Solver.create(crosswordUpdater, slotChooser, candidateChooser, backtracker);
    }

    /**
     * Prints some insights on the given crossword.
     *
     * @param crossword the puzzle to solve
     */
    private static void printPuzzleInsights(final Crossword crossword) {
        final CachedDictionary dictionary = crossword.dictionary();
        final Collection<Slot> slots = crossword.grid().puzzle().slots();
        final BigInteger branches = slots.stream()
                .map(s -> BigInteger.valueOf(dictionary.cachedCandidatesCount(s)))
                .reduce(BigInteger.ONE, BigInteger::multiply);
        final NumberFormat formatter = new DecimalFormat("0.######E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        LOGGER.info(() -> "Total branches (slot variables, pre-pruned): " + formatter.format(branches));
    }

    /**
     * Solve the given puzzle, using the given dictionary.
     *
     * @param puzzleGrid   the puzzle to solve
     * @param externalDictionary the dictionary to use
     * @param progressListener   the progress listener
     * @return the result
     * @throws InterruptedException if interrupted while solving
     */
    public SolverResult solve(
            final PuzzleGrid puzzleGrid, final Dictionary externalDictionary, final ProgressListener progressListener)
            throws InterruptedException {

        progressListener.onInitialisationStart();

        final Crossword crossword = Crossword.create(puzzleGrid, externalDictionary);
        final StatisticsRecorder stats = new StatisticsRecorder();
        final Solver solver = newSolver(crossword, progressListener, stats);
        printPuzzleInsights(crossword);

        progressListener.onInitialisationEnd();

        final boolean solved = solver.solve();

        progressListener.onSolverProgressUpdate((short) 100);

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
    public SolverResult solve(final PuzzleGrid puzzle, final Dictionary dictionary) throws InterruptedException {
        return solve(puzzle, dictionary, ProgressListener.DUMMY_LISTENER);
    }
}
