package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.CrosswordPuzzle;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.CandidateChooser;
import com.gitlab.super7ramp.crosswords.solver.lib.core.CrosswordSolverEngine;
import com.gitlab.super7ramp.crosswords.solver.lib.core.ProbablePuzzle;
import com.gitlab.super7ramp.crosswords.solver.lib.db.AdaptedDictionaryImpl;

final class CrosswordSolverImpl implements CrosswordSolver {

    /**
     * Constructor.
     */
    CrosswordSolverImpl() {
        // Nothing to do.
    }

    @Override
    public SolverResult solve(final CrosswordPuzzle puzzleDefinition, final Dictionary externalDictionary) throws InterruptedException {

        final ProbablePuzzle puzzle = null; // TODO adapt from definition
        final AdaptedDictionary dictionary = new AdaptedDictionaryImpl(externalDictionary);

        final SlotIteratorImpl slotChooser = new SlotIteratorImpl(puzzle, dictionary);
        final CandidateChooser candidateChooser = new CandidateChooserImpl(puzzle, dictionary);
        final Backtracker backtracker = new BacktrackerImpl(puzzle, dictionary);

        new CrosswordSolverEngine(slotChooser, candidateChooser, backtracker).solve();

        return new SolverResult() {
        };
    }
}
