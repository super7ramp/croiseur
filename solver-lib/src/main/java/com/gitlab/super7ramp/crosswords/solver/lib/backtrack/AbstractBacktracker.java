package com.gitlab.super7ramp.crosswords.solver.lib.backtrack;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.history.DeadEnd;
import com.gitlab.super7ramp.crosswords.solver.lib.history.InstantiationHistoryConsumer;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Abstract {@link Backtracker} suitable for implementations always unassigning last assigned
 * variable.
 */
abstract class AbstractBacktracker implements Backtracker {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(Backtrack.class.getName());

    /** Assignment history. */
    private final InstantiationHistoryConsumer instantiationHistory;

    /**
     * Constructor.
     *
     * @param anInstantiationHistory assignment history
     */
    AbstractBacktracker(final InstantiationHistoryConsumer anInstantiationHistory) {
        instantiationHistory = anInstantiationHistory;
    }

    @Override
    public final Optional<Slot> backtrackFrom(final Slot variable) {

        final Optional<Slot> optUnassigned = instantiationHistory.lastAssignedSlot();

        // TODO to move to proper place when cleaning backtrack
        //final Optional<Slot> optUnassigned =
        //        instantiationHistory.lastAssignedConnectedSlot(variable);

        if (optUnassigned.isPresent()) {
            final Slot unassigned = optUnassigned.get();
            updateBlackList(new DeadEnd(variable.uid(), unassigned.uid()), unassigned.value()
                                                                                     .orElseThrow());
        }

        return optUnassigned;
    }

    /**
     * Updates blacklist.
     *
     * @param deadEnd         dead-end situation
     * @param unassignedValue unassigned value
     */
    protected abstract void updateBlackList(final DeadEnd deadEnd, final String unassignedValue);

}
