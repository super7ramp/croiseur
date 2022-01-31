package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.ProgressListener;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;
import com.gitlab.super7ramp.crosswords.solver.lib.core.sap.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.sap.Solver;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.Grid;
import com.gitlab.super7ramp.crosswords.solver.lib.heuristics.backtrack.Backtrackers;
import com.gitlab.super7ramp.crosswords.solver.lib.heuristics.instantiation.CandidateChooserImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.heuristics.iteration.SlotIteratorImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.listener.ProgressNotifier;
import com.gitlab.super7ramp.crosswords.solver.lib.listener.StatisticsRecorder;

import java.util.Collection;

final class CrosswordSolverImpl implements CrosswordSolver {

    /**
     * Constructor.
     */
    CrosswordSolverImpl() {
        // Nothing to do.
    }

    private static SolverResultImpl result(final Grid grid,
                                           final StatisticsRecorder statisticsRecorder,
                                           final boolean success) {
        final SolverResultImpl solverResult;
        if (success) {
            solverResult = SolverResultImpl.success(grid.boxes(), statisticsRecorder.statistics());
        } else {
            solverResult = SolverResultImpl.impossible(grid.boxes(),
                    statisticsRecorder.statistics());
        }
        return solverResult;
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

    @Override
    public SolverResult solve(final PuzzleDefinition puzzleDefinition,
                              final Dictionary externalDictionary,
                              final ProgressListener progressListener) throws InterruptedException {

        progressListener.onInitialisationStart();

        final Crossword crossword = Crossword.create(puzzleDefinition, externalDictionary);
        final StatisticsRecorder stats = new StatisticsRecorder();
        final Solver solver = newSolver(crossword, progressListener, stats);

        progressListener.onInitialisationEnd();

        return result(crossword.grid(), stats, solver.solve());
    }

}
