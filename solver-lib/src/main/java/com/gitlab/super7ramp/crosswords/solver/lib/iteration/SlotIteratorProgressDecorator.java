package com.gitlab.super7ramp.crosswords.solver.lib.iteration;

import com.gitlab.super7ramp.crosswords.solver.api.ProgressListener;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIterator;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

/**
 * A {@link SlotIterator} decorator that indicates progress.
 */
public final class SlotIteratorProgressDecorator implements SlotIterator {

    /**
     * Interval between two progress notifications.
     */
    private static final Duration PROGRESS_NOTIFICATION_INTERVAL = Duration.ofSeconds(1);

    /**
     * Actual iterator.
     */
    private final SlotIterator actual;

    /**
     * All variables.
     */
    private final Collection<Slot> variables;

    /**
     * Progress listener.
     */
    private final ProgressListener progressListener;

    /**
     * Last time a progress indication has been published.
     */
    private Instant lastProgressNotificationTime;

    /**
     * Constructor.
     *
     * @param anActual the actual iterator
     */
    public SlotIteratorProgressDecorator(final SlotIterator anActual, final Collection<Slot> slots,
                                         final ProgressListener aProgressListener) {
        actual = anActual;
        variables = Collections.unmodifiableCollection(slots);
        progressListener = aProgressListener;
        lastProgressNotificationTime = Instant.EPOCH;
    }

    @Override
    public boolean hasNext() {
        return actual.hasNext();
    }

    @Override
    public Slot next() {
        final Instant now = Instant.now();
        if (Duration.between(lastProgressNotificationTime, now).compareTo(PROGRESS_NOTIFICATION_INTERVAL) >= 0) {
            lastProgressNotificationTime = now;
            progressListener.onSolverProgressUpdate(completionPercentage());
        }
        return actual.next();
    }

    /**
     * @return the completion percentage
     */
    private short completionPercentage() {
        final long slotSolved = variables.stream().filter(slot -> slot.value().isPresent()).count();
        return (short) (100 * slotSolved / variables.size());
    }
}
