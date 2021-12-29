package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.ProgressListener;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SolverListener;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

/**
 * A {@link SolverListener} decorator that notifies progress to an external
 * {@link ProgressListener}.
 */
final class ProgressNotifier implements SolverListener {

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
    ProgressNotifier(final Collection<Slot> someSlots, final ProgressListener aProgressListener) {
        slots = Collections.unmodifiableCollection(someSlots);
        progressListener = aProgressListener;
        lastProgressNotificationTime = Instant.EPOCH;
    }

    @Override
    public void onPartialUnassignment(Slot slot) {
        refresh();
    }

    @Override
    public void onUnassignment(Slot slot, String unassignedWord) {
        refresh();
    }

    @Override
    public void onAssignment(Slot slot, String word) {
        refresh();
    }

    /**
     * @return the completion percentage
     */
    private short completionPercentage() {
        final long slotSolved = slots.stream().filter(Slot::hasValue).count();
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
