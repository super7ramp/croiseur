package com.gitlab.super7ramp.crosswords.solver.lib.core;

import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.AbstractSatisfactionProblemSolverEngine;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Concrete implementation of {@link AbstractSatisfactionProblemSolverEngine} for crossword solving.
 */
public final class CrosswordSolverEngine extends AbstractSatisfactionProblemSolverEngine<Slot, String> {

    /**
     * Slot iterator.
     */
    private final Iterator<Slot> slotIterator;

    /**
     * Candidate chooser.
     */
    private final CandidateChooser candidateChooser;

    /**
     * Backtracker.
     */
    private final Backtracker backtracker;

    /**
     * Constructor.
     *
     * @param aSlotIterator     iterator on slots
     * @param aCandidateChooser a {@link CandidateChooser}
     * @param aBacktracker      a {@link Backtracker}
     */
    public CrosswordSolverEngine(Iterator<Slot> aSlotIterator, CandidateChooser aCandidateChooser,
                                 Backtracker aBacktracker) {
        slotIterator = aSlotIterator;
        candidateChooser = aCandidateChooser;
        backtracker = aBacktracker;
    }

    @Override
    protected Iterator<Slot> variables() {
        return slotIterator;
    }

    @Override
    protected Optional<String> candidate(final Slot wordVariable) {
        return candidateChooser.find(wordVariable);
    }

    @Override
    protected Set<Slot> backtrackFrom(final Slot wordVariable) {
        return backtracker.backtrackFrom(wordVariable);
    }

}

