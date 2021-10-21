package com.gitlab.super7ramp.crosswords.solver.lib.comparators;

import com.gitlab.super7ramp.crosswords.solver.lib.core.AdaptedDictionary;
import com.gitlab.super7ramp.crosswords.solver.lib.core.History;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.SlotIdentifier;

import java.util.Comparator;
import java.util.Optional;

/**
 * A collection of {@link Comparator}s.
 */
public final class Comparators {

    /**
     * Private constructor, static methods only.
     */
    private Comparators() {
        // Nothing to do.
    }

    /**
     * Returns a comparator that sort {@link Slot}s by their number of possible values.
     *
     * @param dictionary a dictionary
     * @return a comparator that sort {@link Slot}s by their number of possible values.
     */
    // FIXME inefficient if dictionary results are not cached as dictionary::countPossibleValues may be called several
    //  times on the same slot
    public static Comparator<Slot> byNumberOfCandidates(final AdaptedDictionary dictionary) {
        return Comparator.comparingLong(dictionary::countPossibleValues);
    }

    /**
     * Returns a comparator that sort slots by assignment age.
     *
     * @return a comparator that sort slots by assignment age.
     */
    public static Comparator<Slot> byAssignmentAge(final History history) {
        return Comparator.comparing(Slot::uid, (a, b) -> {
            final Optional<SlotIdentifier> optLastAssignedSlot = history.lastAssignedSlot().map(Slot::uid);
            if (optLastAssignedSlot.isPresent()) {
                final SlotIdentifier lastAssignedSlot = optLastAssignedSlot.get();
                if (a.equals(lastAssignedSlot) && !b.equals(lastAssignedSlot)) {
                    return -1;
                }
                if (!a.equals(lastAssignedSlot) && b.equals(lastAssignedSlot)) {
                    return 1;
                }
            }
            return 0;
        });
    }

}
