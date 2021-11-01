package com.gitlab.super7ramp.crosswords.solver.lib.backtrack;

import com.gitlab.super7ramp.crosswords.solver.lib.core.Backtracker;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.history.DeadEnd;
import com.gitlab.super7ramp.crosswords.solver.lib.history.InstantiationHistoryConsumer;

import java.util.logging.Logger;

/**
 * Abstract {@link Backtracker} suitable for implementations always unassigning last assigned variable.
 */
abstract class AbstractBacktracker implements Backtracker {

    /** Logger. */
    private static Logger LOGGER = Logger.getLogger(Backtrack.class.getName());

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
    public final Slot backtrackFrom(final Slot variable) {
        final Slot unassigned = instantiationHistory.lastAssignedSlot().orElseThrow(IllegalStateException::new);
        unassigned.value().ifPresentOrElse(
                unassignedValue -> updateBlackList(new DeadEnd(variable.uid(), unassigned.uid()), unassignedValue),
                () -> LOGGER.warning("Unassigning a non-complete slot")
        );
        return unassigned;
    }

    /**
     * Updates blacklist.
     *
     * @param deadEnd         dead-end situation
     * @param unassignedValue unassigned value
     */
    protected abstract void updateBlackList(final DeadEnd deadEnd, final String unassignedValue);

}
