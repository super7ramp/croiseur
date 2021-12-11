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
import com.gitlab.super7ramp.crosswords.solver.lib.dictionary.CachedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.Grid;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.GridFactory;
import com.gitlab.super7ramp.crosswords.solver.lib.history.HistoryImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.instantiation.CandidateChooserImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.iterator.SlotIteratorImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.iterator.SlotIteratorProgressDecorator;

import java.util.Collection;

final class CrosswordSolverImpl implements CrosswordSolver {

    /**
     * Constructor.
     */
    CrosswordSolverImpl() {
        // Nothing to do.
    }

    @Override
    public SolverResult solve(final PuzzleDefinition puzzleDefinition, final Dictionary externalDictionary,
                              final ProgressListener progressListener) throws InterruptedException {

        progressListener.onInitialisationStart();

        final Grid grid = GridFactory.createGrid(puzzleDefinition);
        final HistoryImpl history = new HistoryImpl();
        final CachedDictionary dictionary = new CachedDictionary(externalDictionary, history.backtrack());
        final Collection<Slot> slots = grid.puzzle().slots();
        final SlotIteratorProgressDecorator slotChooser =
                new SlotIteratorProgressDecorator(new SlotIteratorImpl(slots, dictionary), slots, progressListener);
        final CandidateChooserImpl candidateChooser =
                new CandidateChooserImpl(grid.puzzle(), dictionary, history.instantiation());
        final Backtracker backtracker = Backtrackers.backmark(history);

        final CrosswordSolverEngine solverEngine = new CrosswordSolverEngine(grid.puzzle(), slotChooser,
                candidateChooser, backtracker, dictionary);

        progressListener.onInitialisationEnd();

        solverEngine.solve();

        return new SolverResultImpl(grid.boxes());
    }

}
