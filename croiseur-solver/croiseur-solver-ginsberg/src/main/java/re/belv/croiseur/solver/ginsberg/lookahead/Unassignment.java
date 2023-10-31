/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.lookahead;

import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

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
        return new Unassignment() {
            @Override
            public SlotIdentifier slotUid() {
                return aSlotUid;
            }

            @Override
            public String toString() {
                return aSlotUid.toString();
            }
        };
    }

    /**
     * The identifier of the slot to unassign.
     *
     * @return the identifier of the slot to unassign.
     */
    SlotIdentifier slotUid();
}
