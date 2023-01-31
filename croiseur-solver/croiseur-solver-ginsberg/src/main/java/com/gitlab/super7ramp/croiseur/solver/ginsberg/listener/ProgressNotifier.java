/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.listener;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.ProgressListener;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

/**
 * A {@link SolverListener} decorator that notifies progress to an external
 * {@link ProgressListener}.
 */
public final class ProgressNotifier implements SolverListener {

    /** Interval between two progress notifications. */
    private static final Duration PROGRESS_NOTIFICATION_INTERVAL = Duration.ofSeconds(1);

    /** All variables. */
    private final Collection<Slot> slots;

    /** Progress listener. */
    private final ProgressListener progressListener;

    /** Last time a progress indication has been published. */
    private Instant lastProgressNotificationTime;

    /**
     * Constructor.
     *
     * @param someSlots         the variables
     * @param aProgressListener the notification callback
     */
    public ProgressNotifier(final Collection<Slot> someSlots,
                            final ProgressListener aProgressListener) {
        slots = Collections.unmodifiableCollection(someSlots);
        progressListener = aProgressListener;
        lastProgressNotificationTime = Instant.EPOCH;
    }

    @Override
    public void onUnassignment(final Slot slot, final String unassignedWord) {
        refresh();
    }

    @Override
    public void onAssignment(final Slot slot, final String word) {
        refresh();
    }

    /**
     * @return the completion percentage
     */
    private short completionPercentage() {
        final long slotSolved = slots.stream().filter(Slot::isInstantiated).count();
        return (short) (100 * slotSolved / slots.size());
    }

    /**
     * Refresh the statistics.
     */
    private void refresh() {
        final Instant now = Instant.now();
        if (Duration.between(lastProgressNotificationTime, now)
                    .compareTo(PROGRESS_NOTIFICATION_INTERVAL) >= 0) {
            lastProgressNotificationTime = now;
            progressListener.onSolverProgressUpdate(completionPercentage());
        }
    }

}
