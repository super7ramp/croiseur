package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.ProgressListener;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import com.gitlab.super7ramp.crosswords.solver.lib.backtrack.Backtrackers;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.CrosswordSolverEngine;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.dictionary.CachedDictionaryImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.Grid;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.GridFactory;
import com.gitlab.super7ramp.crosswords.solver.lib.history.HistoryImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.instantiation.CandidateChooserImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.iteration.SlotIteratorImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.iteration.SlotIteratorProgressDecorator;

import java.util.Collection;

final class CrosswordSolverImpl implements CrosswordSolver {

    /**
     * Constructor.
     */
    CrosswordSolverImpl() {
        // Nothing to do.
    }

    private static SolverResultImpl result(final Grid grid, final boolean success) {
        final SolverResultImpl solverResult;
        if (success) {
            solverResult = SolverResultImpl.success(grid.boxes());
        } else {
            solverResult = SolverResultImpl.impossible(grid.boxes());
        }
        return solverResult;
    }

    @Override
    public SolverResult solve(final PuzzleDefinition puzzleDefinition, final Dictionary externalDictionary,
                              final ProgressListener progressListener) throws InterruptedException {

        progressListener.onInitialisationStart();

        final Grid grid = GridFactory.createGrid(puzzleDefinition);
        final Collection<Slot> slots = grid.puzzle().slots();

        final HistoryImpl history = new HistoryImpl();

        final CachedDictionaryImpl dictionary = new CachedDictionaryImpl(externalDictionary, slots, grid.puzzle(),
                history.backtrack());

        final SlotIteratorProgressDecorator slotChooser =
                new SlotIteratorProgressDecorator(new SlotIteratorImpl(slots, dictionary), slots, progressListener);

        final CandidateChooserImpl candidateChooser = new CandidateChooserImpl(grid.puzzle(), dictionary);

        final Backtracker backtracker = Backtrackers.enhancedBacktrack(history);

        final CrosswordSolverEngine solverEngine = new CrosswordSolverEngine(slotChooser, candidateChooser,
                backtracker).withListener(dictionary).withListener(history.instantiation());

        progressListener.onInitialisationEnd();

        return result(grid, solverEngine.solve());
    }

}
