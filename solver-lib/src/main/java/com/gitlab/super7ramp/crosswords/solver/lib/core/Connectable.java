package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Set;

/**
 * Access to slot connections.
 */
public interface Connectable {

    /**
     * Return the connected slots for given slot.
     *
     * @param aSlot the slot
     * @return slots connected to given slot
     */
    Set<Slot> connectedSlots(final Slot aSlot);
}
