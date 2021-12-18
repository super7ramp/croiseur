package com.gitlab.super7ramp.crosswords.solver.lib.core;

import com.gitlab.super7ramp.crosswords.solver.lib.util.solver.AbstractSatisfactionProblemSolverEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Concrete implementation of {@link AbstractSatisfactionProblemSolverEngine} for crossword solving.
 */
public final class CrosswordSolverEngine extends AbstractSatisfactionProblemSolverEngine<Slot, String> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CrosswordSolverEngine.class.getName());

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
     * Listeners to assignment/unassignment.
     */
    private final Collection<SolverUpdateListener> listeners;

    /**
     * Constructor.
     *
     * @param aSlotIterator     iterator on slots
     * @param aCandidateChooser a {@link CandidateChooser}
     * @param aBacktracker      a {@link Backtracker}
     */
    public CrosswordSolverEngine(final Iterator<Slot> aSlotIterator,
                                 final CandidateChooser aCandidateChooser,
                                 final Backtracker aBacktracker) {
        slotIterator = aSlotIterator;
        candidateChooser = aCandidateChooser;
        backtracker = aBacktracker;
        listeners = new ArrayList<>();
    }

    /**
     * Add a listener to this {@link CrosswordSolverEngine}.
     *
     * @param listener a listener
     * @return this {@link CrosswordSolverEngine}
     */
    public CrosswordSolverEngine withListener(final SolverUpdateListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    protected void assign(final Slot variable, final String value) {
        LOGGER.info(() -> "Assigning [" + value + "] to variable [" + variable + "]");
        variable.assign(value);

        listeners.forEach(listener -> listener.onAssignment(variable, value));
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
    protected boolean backtrackFrom(final Slot unassignable) {
        LOGGER.fine(() -> "No candidate for [" + unassignable + "], backtracking.");
        final Optional<Slot> optToUnassign = backtracker.backtrackFrom(unassignable);
        optToUnassign.ifPresent(this::unassign);
        return optToUnassign.isPresent();
    }

    private void unassign(final Slot toUnassign) {
        LOGGER.info(() -> "Unassigning variable [" + toUnassign + "]");
        final Optional<String> optRemovedValue = toUnassign.unassign();

        optRemovedValue.ifPresentOrElse(
                removedValue -> listeners.forEach(listener -> listener.onUnassignment(toUnassign, removedValue)),
                () -> {
                    LOGGER.warning(() -> "Unassigning non-complete variable " + toUnassign);
                    listeners.forEach(listener -> listener.onPartialUnassignment(toUnassign));
                }
        );
    }
}

