/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;

/**
 * Slot extended with additional data for internal usage in package.
 */
interface InternalSlot extends Slot {

    /**
     * The slot definition.
     *
     * @return the slot definition
     */
    SlotDefinition definition();
}
