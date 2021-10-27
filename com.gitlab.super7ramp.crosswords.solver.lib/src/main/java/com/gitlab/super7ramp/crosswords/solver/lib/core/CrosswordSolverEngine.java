package com.gitlab.super7ramp.crosswords.solver.lib.core;

import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.AbstractSatisfactionProblemSolverEngine;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Concrete implementation of {@link AbstractSatisfactionProblemSolverEngine} for crossword solving.
 */
public final class CrosswordSolverEngine extends AbstractSatisfactionProblemSolverEngine<Slot, String> {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CrosswordSolverEngine.class.getName());

    /** Slot iterator. */
    private final Iterator<Slot> slotIterator;

    /** Candidate chooser. */
    private final CandidateChooser candidateChooser;

    /** Backtracker. */
    private final Backtracker backtracker;

    /** Dictionary. */
    private final InternalDictionary dictionary;

    /** History. */
    private final History history;

    /** The puzzle. TODO move out if possible. */
    private final Connectable puzzle;

    /**
     * Constructor.
     *
     * @param aSlotIterator     iterator on slots
     * @param aCandidateChooser a {@link CandidateChooser}
     * @param aBacktracker      a {@link Backtracker}
     */
    public CrosswordSolverEngine(final Connectable aPuzzle,
                                 final Iterator<Slot> aSlotIterator,
                                 final CandidateChooser aCandidateChooser,
                                 final Backtracker aBacktracker,
                                 final InternalDictionary aDictionary,
                                 final History aHistory) {
        puzzle = aPuzzle;
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
        dictionary.use(value);
    }

    @Override
    protected void onUnassignment(final Slot variable) {
        history.recordUnassignment(variable);

        if (variable.value().isPresent()) {
            final String unassignedValue = variable.value().get();

            // Value can now be reused for another word
            dictionary.free(unassignedValue);

            // This value should not be used again for this variable, each word in the grid should be unique
            dictionary.blacklist(variable, unassignedValue);
        } else {
            LOGGER.warning(() -> "Unassigning non-complete slot " + variable);
        }

        // Connected slots will be amputated from one letter, their previous value shouldn't be reserved anymore
        for (final Slot connectedSlot : puzzle.connectedSlots(variable)) {
            connectedSlot.value().ifPresent(dictionary::free);
        }
    }
}

