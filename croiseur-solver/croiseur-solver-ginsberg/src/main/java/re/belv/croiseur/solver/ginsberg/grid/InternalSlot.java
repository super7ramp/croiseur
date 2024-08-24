/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

import re.belv.croiseur.solver.ginsberg.core.Slot;

/** Slot extended with additional data for internal usage in package. */
interface InternalSlot extends Slot {

    /**
     * The slot definition.
     *
     * @return the slot definition
     */
    SlotDefinition definition();
}
