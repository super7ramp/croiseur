package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.Set;

/**
 * Read/write access to crossword puzzle model.
 */
public interface Puzzle {

    /**
     * Return all the slots.
     *
     * @return all the slots
     */
    Set<Slot> slots();

    /**
     * Return the connected slots for given slot.
     *
     * @param aSlot the slot
     * @return slots connected to given slot
     */
    Set<Slot> connectedSlots(final Slot aSlot);

}
