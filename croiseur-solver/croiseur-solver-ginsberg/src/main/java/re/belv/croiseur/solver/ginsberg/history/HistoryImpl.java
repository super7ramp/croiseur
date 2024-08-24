/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.history;

import java.util.HashMap;
import java.util.Map;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

/** Implementation of {@link History}. */
final class HistoryImpl implements HistoryWriter {

    /** Ages for each assignment. */
    private final Map<SlotIdentifier, Long> assignmentNumbers;

    /** The number of assignments so far. */
    private long assignmentNumber;

    /** Constructs an instance. */
    HistoryImpl() {
        assignmentNumbers = new HashMap<>();
    }

    @Override
    public void addAssignmentRecord(final Slot slot) {
        assignmentNumbers.put(slot.uid(), ++assignmentNumber);
    }

    @Override
    public void removeAssignmentRecord(final Slot slot) {
        assignmentNumbers.remove(slot.uid());
    }

    @Override
    public long assignmentNumber(final SlotIdentifier slotId) {
        return assignmentNumbers.getOrDefault(slotId, Long.MAX_VALUE);
    }
}
