package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import com.gitlab.super7ramp.crosswords.solver.lib.backtrack.Backtrackers;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.CrosswordSolverEngine;
import com.gitlab.super7ramp.crosswords.solver.lib.core.InternalDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.dictionary.CachedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.dictionary.ProgressiveLookupDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.Grid;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.GridFactory;
import com.gitlab.super7ramp.crosswords.solver.lib.history.HistoryImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.instantiation.CandidateChooserImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.iterator.SlotIteratorImpl;

final class CrosswordSolverImpl implements CrosswordSolver {

    private static final long MAX_NUMBER_OF_CANDIDATES_BY_DICTIONARY_SEARCH = 10;

    /**
     * Constructor.
     */
    CrosswordSolverImpl() {
        // Nothing to do.
    }

    @Override
    public SolverResult solve(final PuzzleDefinition puzzleDefinition, final Dictionary externalDictionary) throws InterruptedException {

        final Grid grid = GridFactory.createGrid(puzzleDefinition);
        final InternalDictionary dictionary = new ProgressiveLookupDictionary(
                new CachedDictionary(externalDictionary),
                MAX_NUMBER_OF_CANDIDATES_BY_DICTIONARY_SEARCH);
        final HistoryImpl history = new HistoryImpl();

        final SlotIteratorImpl slotChooser = new SlotIteratorImpl(grid.puzzle().slots(), dictionary);
        final CandidateChooserImpl candidateChooser = new CandidateChooserImpl(grid.puzzle(), dictionary);
        final Backtracker backtracker = Backtrackers.simple(history);
        //final Backtracker backtracker = Backtrackers.smartBackjump(grid.puzzle(), dictionary, history);
        //final Backtracker backtracker = Backtrackers.backjump(grid.puzzle(), history);

        new CrosswordSolverEngine(grid.puzzle(), slotChooser, candidateChooser, backtracker, dictionary, history).solve();

        return new SolverResultImpl(grid.boxes());
    }

}
