package com.gitlab.super7ramp.crosswords.solver.ginsberg.lookahead;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.SlotIdentifier;

/**
 * Represents the unassignment of a slot.
 */
public interface Unassignment {

    /**
     * Builds an {@link Unassignment}.
     *
     * @param aSlotUid an UID
     * @return the {@link Unassignment}
     */
    static Unassignment of(final SlotIdentifier aSlotUid) {
        return () -> aSlotUid;
    }

    /**
     * The identifier of the slot to unassign.
     *
     * @return the identifier of the slot to unassign.
     */
    SlotIdentifier slotUid();
}
