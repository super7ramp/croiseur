package com.gitlab.super7ramp.crosswords.solver.lib.core;

import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.AbstractSatisfactionProblemSolverEngine;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Concrete implementation of {@link AbstractSatisfactionProblemSolverEngine} for crossword solving.
 */
public final class CrosswordSolverEngine extends AbstractSatisfactionProblemSolverEngine<Slot, String> {

    /** Slot iterator. */
    private final Iterator<Slot> slotIterator;

    /** Candidate chooser. */
    private final CandidateChooser candidateChooser;

    /** Backtracker. */
    private final Backtracker backtracker;

    /** Dictionary. */
    private final AdaptedDictionary dictionary;

    /** History. */
    private final History history;

    /**
     * Constructor.
     *
     * @param aSlotIterator     iterator on slots
     * @param aCandidateChooser a {@link CandidateChooser}
     * @param aBacktracker      a {@link Backtracker}
     */
    public CrosswordSolverEngine(final Iterator<Slot> aSlotIterator,
                                 final CandidateChooser aCandidateChooser,
                                 final Backtracker aBacktracker,
                                 final AdaptedDictionary aDictionary,
                                 final History aHistory) {
        slotIterator = aSlotIterator;
        candidateChooser = aCandidateChooser;
        backtracker = aBacktracker;
        dictionary = aDictionary;
        history = aHistory;
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

    @Override
    protected void onAssignment(final Slot variable) {
        final String value = variable.value().orElseThrow(IllegalStateException::new);
        history.recordAssignment(variable, value);

        // Prevent value from being reused for another word
        dictionary.lock(value);
    }

    @Override
    protected void onUnassignment(final Slot variable) {
        history.recordUnassignment(variable);

        // Value can now be reused for another word
        final String unassignedValue = variable.value().orElseThrow(IllegalStateException::new);
        dictionary.unlock(unassignedValue);

        // Prevent this value to be used again for this variable
        dictionary.blacklist(variable.uid(), unassignedValue);
    }
}

