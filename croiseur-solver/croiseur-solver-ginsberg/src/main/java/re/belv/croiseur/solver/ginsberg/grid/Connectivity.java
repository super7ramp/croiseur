/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

import java.util.function.BiPredicate;
import java.util.stream.Stream;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

/**
 * Connectivity between slots.
 */
interface Connectivity extends BiPredicate<SlotIdentifier, SlotIdentifier> {

    /**
     * Returns the slots connected to the given slot as a {@link Stream}.
     *
     * @param slot the slot to get connections from
     * @return the slots connected to the given slot
     */
    Stream<InternalSlot> connectedSlots(final SlotIdentifier slot);
}
