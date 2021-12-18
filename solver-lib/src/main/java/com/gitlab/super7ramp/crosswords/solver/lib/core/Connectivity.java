package com.gitlab.super7ramp.crosswords.solver.lib.core;

import java.util.function.BiPredicate;

/**
 * Access to slot connections.
 */
public interface Connectivity extends BiPredicate<Slot, Slot> {

    @Override
    default boolean test(final Slot slot, final Slot slot2) {
        return areSlotsConnected(slot, slot2);
    }

    /**
     * Return <code>true</code> if the slots are connected.
     *
     * @param first  first slot
     * @param second second slot
     * @return <code>true</code> if the slots are connected
     */
    default boolean areSlotsConnected(final Slot first, final Slot second) {
        return areSlotsConnected(first.uid(), second.uid());
    }

    /**
     * Return <code>true</code> if the slots identified by the given identifiers are connected.
     *
     * @param first  first slot identifier
     * @param second second slot identifier
     * @return <code>true</code> if the slots identified by the given identifiers are connected
     */
    boolean areSlotsConnected(final SlotIdentifier first, final SlotIdentifier second);

}
