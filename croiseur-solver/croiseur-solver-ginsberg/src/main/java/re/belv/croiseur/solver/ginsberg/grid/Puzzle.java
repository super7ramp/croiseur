/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.grid;

import java.util.Collection;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

/** Read/write access to crossword puzzle model. */
public interface Puzzle {

    /**
     * The slots.
     *
     * @return the slots
     */
    Collection<Slot> slots();

    /**
     * The slot with given identifier.
     *
     * @param slotIdentifier the identifier
     * @return the slot with given identifer
     */
    Slot slot(SlotIdentifier slotIdentifier);

    /**
     * Returns a deep copy of this puzzle.
     *
     * @return a deep copy of this puzzle
     */
    Puzzle copy();
}
