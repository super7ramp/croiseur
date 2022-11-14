package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.Solver;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.heuristics.backtrack.Backtrackers;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.heuristics.instantiation.CandidateChooserImpl;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.heuristics.iteration.SlotIteratorImpl;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.listener.ProgressNotifier;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.listener.StatisticsRecorder;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.result.SolverResultFactory;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.state.Crossword;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.state.CrosswordUpdater;

import java.util.Collection;

/**
 * A crosswords solver.
 */
public final class GinsbergCrosswordSolver {

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
        final CandidateChooserImpl candidateChooser =
                new CandidateChooserImpl(problem.grid().puzzle(), problem.dictionary());
        final Backtracker<Slot, SlotIdentifier> backtracker =
                Backtrackers.defaultBacktrack(problem.history());

        // A random listener
        final ProgressNotifier progressNotifier = new ProgressNotifier(slots, progressListener);

        // The internal state updater
        final CrosswordUpdater crosswordUpdater =
                new CrosswordUpdater(problem).withListeners(progressNotifier, statisticsRecorder);

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

        progressListener.onInitialisationEnd();

        boolean solved = solver.solve();

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
