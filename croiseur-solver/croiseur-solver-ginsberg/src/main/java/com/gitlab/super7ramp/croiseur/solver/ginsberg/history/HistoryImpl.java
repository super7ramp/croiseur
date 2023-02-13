/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.history;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Implementation of {@link History}.
 */
final class HistoryImpl implements HistoryWriter {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(HistoryImpl.class.getName());

    /** Ages for each assignment. */
    private final Map<SlotIdentifier, Long> assignmentDates;

    /** The current age. */
    private long currentDate;

    private long tsStart;

    /**
     * Constructs an instance.
     */
    HistoryImpl() {
        assignmentDates = new HashMap<>();
        currentDate = 1;
    }

    @Override
    public void addAssignmentRecord(final Slot slot) {
        assignmentDates.put(slot.uid(), getAndIncrementCurrentDate());
    }

    @Override
    public void removeAssignmentRecord(final Slot slot) {
        assignmentDates.remove(slot.uid());
    }

    @Override
    public long assignmentDate(final SlotIdentifier slotId) {
        return assignmentDates.getOrDefault(slotId, Long.MAX_VALUE);
    }

    /**
     * Gets and increments the current date.
     *
     * @return the current date, before incrementation
     */
    private long getAndIncrementCurrentDate() {
        if (currentDate == 1) {
            tsStart = System.currentTimeMillis();
        }
        if (currentDate % 1_000 == 0) {
            final long now = System.currentTimeMillis();
            LOGGER.info("Current age = " + currentDate);
            System.out.println("Current age = " + currentDate);
            System.out.println("Time spent for 1_000 = " + (now - tsStart));
            tsStart = now;
        }
        return currentDate++;
    }
}
