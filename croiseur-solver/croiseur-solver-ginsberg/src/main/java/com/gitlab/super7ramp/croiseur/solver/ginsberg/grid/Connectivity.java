/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

import java.util.function.BiPredicate;
import java.util.stream.Stream;

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
