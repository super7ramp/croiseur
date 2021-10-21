package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.CrosswordSolverEngine;
import com.gitlab.super7ramp.crosswords.solver.lib.core.History;
import com.gitlab.super7ramp.crosswords.solver.lib.db.AdaptedDictionaryImpl;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.Grid;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.GridFactory;
import com.gitlab.super7ramp.crosswords.solver.lib.history.HistoryImpl;

final class CrosswordSolverImpl implements CrosswordSolver {

    /**
     * Constructor.
     */
    CrosswordSolverImpl() {
        // Nothing to do.
    }

    @Override
    public SolverResult solve(final PuzzleDefinition puzzleDefinition, final Dictionary externalDictionary) throws InterruptedException {

        final Grid grid = GridFactory.createGrid(puzzleDefinition);
        final AdaptedDictionary dictionary = new AdaptedDictionaryImpl(externalDictionary);
        final History history = new HistoryImpl();

        final SlotIteratorImpl slotChooser = new SlotIteratorImpl(grid.puzzle(), dictionary);
        final CandidateChooserImpl candidateChooser = new CandidateChooserImpl(grid.puzzle(), dictionary);
        final BacktrackerImpl backtracker = new BacktrackerImpl(grid.puzzle(), dictionary, history);

        new CrosswordSolverEngine(slotChooser, candidateChooser, backtracker, dictionary, history).solve();

        return grid::boxes;
    }

}
