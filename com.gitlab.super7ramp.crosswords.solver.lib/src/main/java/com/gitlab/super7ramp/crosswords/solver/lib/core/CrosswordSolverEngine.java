package com.gitlab.super7ramp.crosswords.solver.lib.core;

import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.AbstractSatisfactionProblemSolverEngine;

import java.util.Iterator;
import java.util.Optional;
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

    /** The puzzle. */
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
                                 final InternalDictionary aDictionary) {
        puzzle = aPuzzle;
        slotIterator = aSlotIterator;
        candidateChooser = aCandidateChooser;
        backtracker = aBacktracker;
        dictionary = aDictionary;
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
    protected Slot backtrackFrom(final Slot unassignable) {
        return backtracker.backtrackFrom(unassignable);
    }

    @Override
    protected void onAssignment(final Slot variable) {
        final String value = variable.value().orElseThrow(IllegalStateException::new);
        // Prevent value from being reused for another word
        dictionary.use(value);
    }

    @Override
    protected void onUnassignment(final Slot variable) {
        // Value can now be reused for another word
        variable.value().ifPresentOrElse(
                dictionary::free,
                () -> LOGGER.warning(() -> "Unassigning non-complete slot " + variable)
        );

        // Connected slots will be amputated from one letter, their previous value shouldn't be reserved anymore
        for (final Slot connectedSlot : puzzle.connectedSlots(variable)) {
            connectedSlot.value().ifPresent(dictionary::free);
        }
    }
}

